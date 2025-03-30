package org.example.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import static org.example.api.ApiConstants.BASE_URL;


public abstract class BaseApiMethod {
    private static RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .addHeader("Content-Type", "application/json")
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .build();

    public static Response sendDeleteRequest(String endpoint, String accessToken){

        return given()
                .spec(requestSpecification)
                .header("Authorization",  accessToken)
                .when()
                .delete(endpoint);
    }

    public static Response sendPostRequest(String endpoint, String body){
        return given()
                .spec(requestSpecification)
                .body(body)
                .when()
                .post(endpoint);

    }
    public static Response sendGetRequest(String endpoint, String body){
        return given()
                .spec(requestSpecification)
                .body(body)
                .when()
                .get(endpoint);

    }

}


