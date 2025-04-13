package org.example.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerResponseModel {
    private boolean success;
    private String message;
    private String accessToken;
    private String refreshToken;

    // Вложенный класс теперь статический
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private String email;
        private String name;
    }

    // Добавляем объект типа User
    private User user;
}