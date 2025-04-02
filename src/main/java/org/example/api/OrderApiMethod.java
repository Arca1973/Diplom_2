package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.example.api.ApiConstants.ORDERS_ENDPOINT;

public class OrderApiMethod extends BaseApiMethod {
    @Step("Получение списка заказов")
    public Response getOrderList(String accessToken) {
        return sendGetRequest(ORDERS_ENDPOINT, accessToken, "");
    }

    @Step("Создание заказов")
    public Response CreateOrder(String accessToken, String... ingredients) {
        StringBuilder jsonBuilder = new StringBuilder("{");
        jsonBuilder.append("\"ingredients\": [");
        for (int i = 0; i < ingredients.length; i++) {
            jsonBuilder.append("\"").append(ingredients[i]).append("\"");
            if (i != ingredients.length - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]}");
        String json = jsonBuilder.toString();
        return sendPostRequest(ORDERS_ENDPOINT, accessToken, json);
    }
}
