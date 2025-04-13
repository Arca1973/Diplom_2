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
        Response response = new UserApiMethod().loginUser(email, password, name);
        var responseData = response.as(ServerResponseModel.class);
        accessToken = responseData.getAccessToken();
    }

    @Test
    @DisplayName("Успешное изменение EMAIL пользователя c авторизацией")
    @Description("Отправляем API запрос с  EMAIL, в полученном ответе проверяем поля success, EMAIL и статус код")
    public void editUserEmailWithAuthorizationTest() {
        String newEmail = "new" + email;
        Response response = new UserApiMethod().editUserData(accessToken, newEmail, password, name);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        Assert.assertEquals(newEmail, responseData.getUser().getEmail());
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        Assert.assertTrue(responseData.isSuccess());
    }

    @Test
    @DisplayName("Успешное изменение NAME пользователя c авторизацией")
    @Description("Отправляем API запрос с  NAME, в полученном ответе проверяем поля success, NAME и статус код")
    public void editUserNameWithAuthorizationTest() {
        String newName = "new" + name;
        Response response = new UserApiMethod().editUserData(accessToken, email, password, newName);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        Assert.assertEquals(newName, responseData.getUser().getName());
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        Assert.assertTrue(responseData.isSuccess());
    }

    @Test
    @DisplayName("Без успешное изменение EMAIL пользователя без авторизацией")
    @Description("Отправляем API запрос с  EMAIL но без авторизации, в полученном ответе проверяем поля success, message и статус код")
    public void editUserEmailWithOutAuthorizationTest() {
        String newEmail = "new" + email;
        Response response = new UserApiMethod().editUserData("", newEmail, password, name);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        Assert.assertFalse(responseData.isSuccess());
        Assert.assertEquals("You should be authorised", responseData.getMessage());
    }

    @Test
    @DisplayName("Без успешное изменение NAME пользователя без авторизацией")
    @Description("Отправляем API запрос с  NAME но без авторизации, в полученном ответе проверяем поля success, message и статус код")
    public void editUserNameWithOutAuthorizationTest() {
        String newName = "new" + name;
        Response response = new UserApiMethod().editUserData("", email, password, newName);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        Assert.assertFalse(responseData.isSuccess());
        Assert.assertEquals("You should be authorised", responseData.getMessage());
    }

    @After
    public void tearDown() {
        accessToken = new UserApiMethod().deleteUser(accessToken);
    }
}
