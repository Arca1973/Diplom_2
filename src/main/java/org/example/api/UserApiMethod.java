package org.example.api;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.example.api.ApiConstants.*;

public class UserApiMethod extends BaseApiMethod {
    private static final Gson gson = new Gson(); // Используем Gson для сериализации

    @Step("Запрос на создание пользоваеля")
    public Response createUser(String email, String password, String name) {

        UserModel.UserData courierData = new UserModel.UserData(email, password, name);
        String body = gson.toJson(courierData);
        return sendPostRequest(CREATE_USER_ENDPOINT, body);
    }

    @Step("Запрос на авторизацию пользоваеля")
    public Response loginUser(String email, String password, String name) {

        UserModel.UserData courierData = new UserModel.UserData(email, password, name);
        String body = gson.toJson(courierData);
        return sendPostRequest(LOGIN_USER_ENDPOINT, body);
    }
    @Step("Запрос на удаление пользоваеля")
    public String deleteUser(String accessToken) {
        if (accessToken!=null) {
            sendDeleteRequest(USER_INFO_ENDPOINT, accessToken);
           return null;
        }
        return accessToken;
    }

}
