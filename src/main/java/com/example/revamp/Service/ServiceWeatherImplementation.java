package com.example.revamp.Service;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;


@Service
@Transactional
@Slf4j
public class ServiceWeatherImplementation implements ServiceWeather{


    private JsonObject returnObject = new JsonObject();
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=0f178737226e7b6a135027a5503d9b14";

    public ResponseEntity getAllInfo(String city) {
        String result = restTemplate.getForObject(String.format(url, city), String.class);

        if (result != null) {
            JsonElement jsonElement = JsonParser.parseString(result);

            String description = jsonElement.getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
            String icon = jsonElement.getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
            float temperature = jsonElement.getAsJsonObject().getAsJsonObject("main").get("temp").getAsFloat();
            float temperatureMax = jsonElement.getAsJsonObject().getAsJsonObject("main").get("temp_max").getAsFloat();
            float temperatureMin = jsonElement.getAsJsonObject().getAsJsonObject("main").get("temp_min").getAsFloat();
            String name = jsonElement.getAsJsonObject().get("name").getAsString();

            returnObject.addProperty("description", description);
            returnObject.addProperty("icon", icon);
            returnObject.addProperty("temperature", temperature);
            returnObject.addProperty("temperatureMax", temperatureMax);
            returnObject.addProperty("temperatureMin", temperatureMin);
            returnObject.addProperty("name", name);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=UTF-8");
            headers.add("X-Fsl-Location", "/");
            headers.add("X-Fsl-Response-Code", "302");

            return new ResponseEntity<>(returnObject.toString(), headers, HttpStatus.OK);

        }


        return null;
    }

    @Override
    public ResponseEntity getTheTemperature(String city) {


        String result = restTemplate.getForObject(String.format(url,city), String.class);


        if (result != null) {
            JsonElement jsonElement = JsonParser.parseString(result);

            float temperature = jsonElement.getAsJsonObject().getAsJsonObject("main").get("temp").getAsFloat();
            float temperatureMax = jsonElement.getAsJsonObject().getAsJsonObject("main").get("temp_max").getAsFloat();
            float temperatureMin = jsonElement.getAsJsonObject().getAsJsonObject("main").get("temp_min").getAsFloat();
            String name = jsonElement.getAsJsonObject().get("name").getAsString();


            returnObject.addProperty("temperature", temperature);
            returnObject.addProperty("temperatureMax", temperatureMax);
            returnObject.addProperty("temperatureMin", temperatureMin);
            returnObject.addProperty("name", name);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=UTF-8");
            headers.add("X-Fsl-Location", "/");
            headers.add("X-Fsl-Response-Code", "302");


            return new ResponseEntity<>(returnObject.toString(), headers, HttpStatus.OK);

        }


        return null;

    }

    @Override
    public ResponseEntity getDescription(String city) {
        String result = restTemplate.getForObject(String.format(url, city), String.class);

        if (result != null) {
            JsonElement jsonElement = JsonParser.parseString(result);

            String description = jsonElement.getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
            String icon = jsonElement.getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

            String name = jsonElement.getAsJsonObject().get("name").getAsString();

            returnObject.addProperty("description", description);
            returnObject.addProperty("icon", icon);
            returnObject.addProperty("name", name);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=UTF-8");
            headers.add("X-Fsl-Location", "/");
            headers.add("X-Fsl-Response-Code", "302");

            return new ResponseEntity<>(returnObject.toString(), headers, HttpStatus.OK);

        }


        return null;
    }


}
