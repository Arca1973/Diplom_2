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

public class EditUserDataTest {
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
        Response response = new UserApiMethod().loginUser(EMAIL, PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        ACCESS_TOKEN  = responseData.accessToken;
    }

    @Test
    @DisplayName("Успешное изменение EMAIL пользователя c авторизацией")
    @Description("Отправляем API запрос с  EMAIL, в полученном ответе проверяем поля success, EMAIL и статус код")
    public void editUserEmailWithAuthorizationTest() {
        String NEW_EMAIL = "new" + EMAIL;
        Response response = new UserApiMethod().editUserData(ACCESS_TOKEN , NEW_EMAIL, PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        Assert.assertEquals(NEW_EMAIL, responseData.user.email);
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        Assert.assertTrue((boolean) responseData.success);
    }

    @Test
    @DisplayName("Успешное изменение NAME пользователя c авторизацией")
    @Description("Отправляем API запрос с  NAME, в полученном ответе проверяем поля success, NAME и статус код")
    public void editUserNameWithAuthorizationTest() {
        String NEW_NAME = "new" + NAME;
        Response response = new UserApiMethod().editUserData(ACCESS_TOKEN , EMAIL, PASSWORD, NEW_NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        Assert.assertEquals(NEW_NAME, responseData.user.name);
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        Assert.assertTrue((boolean) responseData.success);
    }

    @Test
    @DisplayName("Без успешное изменение EMAIL пользователя без авторизацией")
    @Description("Отправляем API запрос с  EMAIL но без авторизации, в полученном ответе проверяем поля success, message и статус код")
    public void editUserEmailWithOutAuthorizationTest() {
        String NEW_EMAIL = "new" + EMAIL;
        Response response = new UserApiMethod().editUserData("", NEW_EMAIL, PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        Assert.assertFalse((boolean) responseData.success);
        Assert.assertEquals("You should be authorised", responseData.message);
    }

    @Test
    @DisplayName("Без успешное изменение NAME пользователя без авторизацией")
    @Description("Отправляем API запрос с  NAME но без авторизации, в полученном ответе проверяем поля success, message и статус код")
    public void editUserNameWithOutAuthorizationTest() {
        String NEW_NAME = "new" + NAME;
        Response response = new UserApiMethod().editUserData("", EMAIL, PASSWORD, NEW_NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        Assert.assertFalse((boolean) responseData.success);
        Assert.assertEquals("You should be authorised", responseData.message);
    }

    @After
    public void tearDown() {
        ACCESS_TOKEN  = new UserApiMethod().deleteUser(ACCESS_TOKEN );
    }
}
