package org.example.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserModel {
    // Класс для хранения данных пользователя
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserData {
        private String email;
        private String password;
        private String name;
    }
}