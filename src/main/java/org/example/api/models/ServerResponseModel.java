package org.example.api.models;


public class ServerResponseModel {
    public boolean success;
    public String message;
    public String accessToken;
    public String refreshToken;

    // Вложенный класс теперь статический

    public static class User {
        public String email;
        public String name;
    }

    // Добавляем объект типа User
    public User user;
}