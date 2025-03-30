import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.api.ServerResponseModel;
import org.example.api.UserApiMethod;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.example.api.ApiConstants.*;

public class LoginUserTest {
    String  ACCESSETOKEN = null;
    // Данные пользователя
    private String EMAIL;
    private String PASSWORD ;
    private String NAME;
    int statusCode = 0;
    @Before
    @Step("Отправляем запрос на  создание пользователя")

    public void setUp() {
        EMAIL = "ninja" + (int) (Math.random() * 1000000)+"@yandex.ru";
        PASSWORD = "1234" + (int) (Math.random() * 1000000);
        NAME = "saske" + (int) (Math.random() * 1000000);
       new UserApiMethod().createUser(EMAIL, PASSWORD, NAME);
    };

    @Test
    @DisplayName("Успешная авторизация пользователя при вводе валидных данных")
    public void validDataUserLoginTest() {
        Response response=  new UserApiMethod().loginUser(EMAIL, PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        ACCESSETOKEN = responseData.accessToken;
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        Assert.assertTrue((boolean) responseData.success);
    }


    @Test
    @DisplayName("Безуспешная попытка авторизации пользователя с неверным EMAIL")

    public void wrongEmailUserLoginTest() {
        Response response=  new UserApiMethod().loginUser("WRONG_EMAIL", PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        ACCESSETOKEN = responseData.accessToken;
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        Assert.assertFalse((boolean) responseData.success);
        Assert.assertEquals("email or password are incorrect", responseData.message);
    }

    @Test
    @DisplayName("Безуспешная попытка авторизации пользователя с неверным  PASSWORD")

    public void wrongPasswordUserLoginTest() {
        Response response=  new UserApiMethod().loginUser(EMAIL, "WRONG_PASSWORD", NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        ACCESSETOKEN = responseData.accessToken;
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        Assert.assertFalse((boolean) responseData.success);
        Assert.assertEquals("email or password are incorrect", responseData.message);
    }

    @After
    public void tearDown() {
        ACCESSETOKEN = new UserApiMethod().deleteUser(ACCESSETOKEN);
    }


}
