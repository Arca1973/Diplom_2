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
    String accessToken = null;
    // Данные пользователя
    private String email;
    private String password;
    private String name;
    int statusCode = 0;

    @Before
    @Step("Отправляем запрос на  создание пользователя")
    public void setUp() {
        // Назначение значений аргументам
        email = TestDataGenerator.generateRandomEmail();
        password = TestDataGenerator.generateRandomPassword();
        name = TestDataGenerator.generateRandomName();

        new UserApiMethod().createUser(email, password, name);
    }

    @Test
    @DisplayName("Успешная авторизация пользователя при вводе валидных данных")
    @Description("Отправляем API запрос с валидными данными, в полученном ответе проверяем поле success и статус код")
    public void validDataUserLoginTest() {
        Response response = new UserApiMethod().loginUser(email, password, name);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        accessToken = responseData.getAccessToken();
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        Assert.assertTrue(responseData.isSuccess());
    }

    @Test
    @DisplayName("Безуспешная попытка авторизации пользователя с неверным EMAIL")
    @Description("Отправляем API запрос с неверным EMAIL, в полученном ответе проверяем поля success, message и статус код")
    public void wrongEmailUserLoginTest() {
        Response response = new UserApiMethod().loginUser("WRONG_EMAIL", password, name);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        accessToken = responseData.getAccessToken();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        Assert.assertFalse(responseData.isSuccess());
        Assert.assertEquals("email or password are incorrect", responseData.getMessage());
    }

    @Test
    @DisplayName("Безуспешная попытка авторизации пользователя с неверным  PASSWORD")
    @Description("Отправляем API запрос с неверным PASSWORD, в полученном ответе проверяем поля success, message и статус код")
    public void wrongPasswordUserLoginTest() {
        Response response = new UserApiMethod().loginUser(email, "WRONG_PASSWORD", name);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        accessToken = responseData.getAccessToken();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        Assert.assertFalse(responseData.isSuccess());
        Assert.assertEquals("email or password are incorrect", responseData.getMessage());
    }

    @After
    public void tearDown() {
        accessToken = new UserApiMethod().deleteUser(accessToken);
    }
}
