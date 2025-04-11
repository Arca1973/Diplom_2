package org.example.api.utils;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.api.models.OrderModel;

import java.util.Arrays;
import java.util.List;

import static org.example.api.utils.ApiConstants.ORDERS_ENDPOINT;

public class OrderApiMethod extends BaseApiMethod {
    @Step("Получение списка заказов")
    public Response getOrderList(String accessToken) {
        return sendGetRequest(ORDERS_ENDPOINT, accessToken, "");
    }

    @Step("Создание заказов")
    public Response CreateOrder(String accessToken, String... ingredients) {

        List<String> ingredientList = Arrays.asList(ingredients);
        OrderModel orderRequest = new OrderModel(ingredientList, null, null, 0, null, null);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(orderRequest);

        return sendPostRequest(ORDERS_ENDPOINT, accessToken, jsonBody);
    }
}
