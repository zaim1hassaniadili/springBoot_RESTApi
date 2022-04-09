package com.example.revamp.Service;

import com.example.revamp.Utils.DesktopApi;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import javax.transaction.Transactional;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@Service
@Transactional
@Slf4j
public class ServiceGmailAPIImplementation implements ServiceGmailAPI {
    private static final String APPLICATION_NAME = "Gmail API";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private String user = "me";

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();


    public ServiceGmailAPIImplementation() throws Exception {
    }


    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = ServiceGmailAPIImplementation.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        AuthorizationCodeInstalledApp.Browser CustomBrowser = new AuthorizationCodeInstalledApp.Browser() {
            @Override
            public void browse(String url) throws IOException {
                System.out.println("url:" + url);
                Preconditions.checkNotNull(url);
                DesktopApi.browse(URI.create(url));

            }
        };
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).setLandingPages("http://localhost:3000/dashboard", "http://localhost:3000/error").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver, CustomBrowser).authorize("user");

        System.out.println("my access_token:" + credential.getAccessToken());
        System.out.println("my refresh_token:" + credential.getRefreshToken());
        return credential;

    }

    public List<String> getIds(int nbr) throws IOException {
        List<String> allIds = new ArrayList<>();
        Gmail.Users.Messages.List request = service.users().messages().list(user);
        for (int i = 0; i < nbr; i++) {
            ListMessagesResponse messagesResponse = request.execute();
            request.setPageToken(messagesResponse.getNextPageToken());
            String messageId = messagesResponse.getMessages().get(i).getId();
            allIds.add(messageId);
        }
        return allIds;
    }

    @Override
    public List<String> getEmail(int nbr) throws IOException {
        List<String> allId = getIds(nbr);
        List<String> listOfEmail = new ArrayList<>();
        for (String msg : allId) {
            try {
                Message message = service.users().messages().get(user, msg).execute();
                String emailBody = StringUtils.newStringUtf8(Base64.decodeBase64(message.getPayload().getParts().get(0).getBody().getData()));
                listOfEmail.add(emailBody);
            } catch (Exception ex) {
                ex.getMessage();
            } finally {
                continue;
            }
        }
        return listOfEmail;
    }


    @Override
    public int getNumberOfEmail() throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getCredentials(HTTP_TRANSPORT).getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        String url ="https://gmail.googleapis.com/gmail/v1/users/me/profile?key=AIzaSyCGKgzHAXutgD1pAYgqAyk0UWKZZ2V2QRI";
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JsonElement jsonElement = JsonParser.parseString(result.getBody());
        String estimateSize= jsonElement.getAsJsonObject().get("messagesTotal").getAsString();

        return Integer.parseInt(estimateSize);
    }

  @Override
    public boolean deleteEmail(int nbr) throws IOException{
        List<String> allIds = getIds(nbr);

      RestTemplate restTemplate = new RestTemplate();

      HashMap<String,List<String>> BatchDelete =new HashMap();
      BatchDelete.put("ids", allIds);

      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(getCredentials(HTTP_TRANSPORT).getAccessToken());
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

      HttpEntity<HashMap<String, List<String>>> entity = new HttpEntity<>(BatchDelete, headers);
      //HttpEntity entity = new HttpEntity<>("body", headers);
      String url ="https://gmail.googleapis.com/gmail/v1/users/me/messages/batchDelete?key=AIzaSyCGKgzHAXutgD1pAYgqAyk0UWKZZ2V2QRI";

      ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

      System.out.println(result);

      return true;

    }

    @Override
    public boolean deleteAllEmail() throws IOException {
       int totalNumberOfMail = getNumberOfEmail();
       return deleteEmail(totalNumberOfMail);
    }

    @Override
    public List<Label> getLabels() throws IOException {
        ListLabelsResponse listResponse = service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        return labels;
    }
}
