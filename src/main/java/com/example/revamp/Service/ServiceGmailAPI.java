package com.example.revamp.Service;

import com.google.api.services.gmail.model.Label;

import java.io.IOException;

import java.util.List;

public interface ServiceGmailAPI {
    List<String> getEmail(int nbr) throws IOException;
    int getNumberOfEmail() throws IOException;
    boolean deleteEmail(int nbr) throws IOException;
    boolean deleteAllEmail() throws IOException;
    List<Label> getLabels() throws IOException;
}
