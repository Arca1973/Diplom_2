package org.example;

public class ApiConstants {
    public class ApiConstants {
        public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

        public static final String CREATE_USER_ENDPOINT = "/api/auth/register";
        public static final String LOGIN_USER_ENDPOINT = "/api/auth/login";
        public static final String LOGOUT_USER_ENDPOINT = "/api/auth/logout";
        public static final String USER_INFO_ENDPOINT = "/api/auth/user";

        public static final String ORDERS_ENDPOINT = "/api/orders";

        // Данные пользователя
        public static final String EMAIL = "ninja" + (int) (Math.random() * 1000000)+"@yandex.ru";
        public static final String PASSWORD = "1234" + (int) (Math.random() * 1000000);
        public static final String NAME = "saske" + (int) (Math.random() * 1000000);
        public static final String  accessToken;
        public static final String  refreshToken;

    }
}
