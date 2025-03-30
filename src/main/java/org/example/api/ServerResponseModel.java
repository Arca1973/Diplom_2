package org.example.api;

public class ServerResponseModel {
    public boolean success;
    public String message;
    public String accessToken;
    public String refreshToken;
       public static class User {
        public String email;
        public String name;
    }
}
