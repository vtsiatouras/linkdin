package com.linkdin.app.dto;

public class Credentials {
    public String email;
    public String password;

    public boolean checkForEmptyFCreds(Credentials credentials) {
        if (credentials.email == null || credentials.password == null) {
            return true;
        }
        return false;
    }
}