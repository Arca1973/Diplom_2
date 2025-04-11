package org.example.api.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.api.models.UserModel;

import static org.example.api.utils.ApiConstants.*;

public class UserApiMethod extends BaseApiMethod {

    @Step("Запрос на создание пользователя")
    public Response createUser(String email, String password, String name) {
        UserModel.UserData userData = new UserModel.UserData(email, password, name);
        return sendPostRequest(CREATE_USER_ENDPOINT,"", userData);
    }

    @Step("Запрос на авторизацию пользователя")
    public Response loginUser(String email, String password, String name) {
        UserModel.UserData userData = new UserModel.UserData(email, password, name);
        return sendPostRequest(LOGIN_USER_ENDPOINT,"", userData);
    }
    @Step("Запрос на изменение данных пользователя")
    public Response editUserData(String accessToken, String email, String password, String name) {
        UserModel.UserData userData = new UserModel.UserData(email, password, name);
        return sendPatchRequest( USER_INFO_ENDPOINT, accessToken, userData);
    }
    @Step("Запрос на удаление пользователя")
    public String deleteUser(String accessToken) {
        if (accessToken!=null) {
            sendDeleteRequest(USER_INFO_ENDPOINT, accessToken);
           return null;
        }
        return accessToken;
    }
}
