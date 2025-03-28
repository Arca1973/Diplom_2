package org.example.api;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.example.api.ApiConstants.CREATE_USER_ENDPOINT;

public class UserApiMethod extends BaseApiMethod {
    private static final Gson gson = new Gson(); // Используем Gson для сериализации

    @Step("Запрос на создание пользоваеля")
    public Response createUser(String email, String password, String name) {

        UserModel.UserData courierData = new UserModel.UserData(email, password, name);
        String body = gson.toJson(courierData);
        return sendPostRequest(CREATE_USER_ENDPOINT, body);
    }
}
