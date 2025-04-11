import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.api.models.ServerResponseModel;
import org.example.api.utils.TestDataGenerator;
import org.example.api.utils.UserApiMethod;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoginUserTest {
    String ACCESS_TOKEN  = null;
    // Данные пользователя
    private String EMAIL;
    private String PASSWORD;
    private String NAME;
    int statusCode = 0;

    @Before
    @Step("Отправляем запрос на  создание пользователя")
    public void setUp() {
        // Назначение значений аргументам
        EMAIL = TestDataGenerator.generateRandomEmail();
        PASSWORD = TestDataGenerator.generateRandomPassword();
        NAME = TestDataGenerator.generateRandomName();

        new UserApiMethod().createUser(EMAIL, PASSWORD, NAME);
    }

    @Test
    @DisplayName("Успешная авторизация пользователя при вводе валидных данных")
    @Description("Отправляем API запрос с валидными данными, в полученном ответе проверяем поле success и статус код")
    public void validDataUserLoginTest() {
        Response response = new UserApiMethod().loginUser(EMAIL, PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        ACCESS_TOKEN  = responseData.accessToken;
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        Assert.assertTrue((boolean) responseData.success);
    }

    @Test
    @DisplayName("Безуспешная попытка авторизации пользователя с неверным EMAIL")
    @Description("Отправляем API запрос с неверным EMAIL, в полученном ответе проверяем поля success, message и статус код")
    public void wrongEmailUserLoginTest() {
        Response response = new UserApiMethod().loginUser("WRONG_EMAIL", PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        ACCESS_TOKEN  = responseData.accessToken;
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        Assert.assertFalse((boolean) responseData.success);
        Assert.assertEquals("email or password are incorrect", responseData.message);
    }

    @Test
    @DisplayName("Безуспешная попытка авторизации пользователя с неверным  PASSWORD")
    @Description("Отправляем API запрос с неверным PASSWORD, в полученном ответе проверяем поля success, message и статус код")
    public void wrongPasswordUserLoginTest() {
        Response response = new UserApiMethod().loginUser(EMAIL, "WRONG_PASSWORD", NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        ACCESS_TOKEN  = responseData.accessToken;
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        Assert.assertFalse((boolean) responseData.success);
        Assert.assertEquals("email or password are incorrect", responseData.message);
    }

    @After
    public void tearDown() {
        ACCESS_TOKEN  = new UserApiMethod().deleteUser(ACCESS_TOKEN );
    }
}
