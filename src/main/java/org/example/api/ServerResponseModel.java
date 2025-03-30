package org.example.api;

public class ServerResponseModel {
    public boolean success;
    public String message;
    public String accessToken;
    public String refreshToken;
       public static class User {
        public static String email;
        public static String name;
    }
}
