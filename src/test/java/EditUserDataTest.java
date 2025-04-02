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
        EMAIL = "ninja" + (int) (Math.random() * 1000000) + "@yandex.ru";
        PASSWORD = "1234" + (int) (Math.random() * 1000000);
        NAME = "saske" + (int) (Math.random() * 1000000);
        new UserApiMethod().createUser(EMAIL, PASSWORD, NAME);
        Response response = new UserApiMethod().loginUser(EMAIL, PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        ACCESS_TOKEN  = responseData.accessToken;
    }

    @Test
    @DisplayName("Успешное изменение EMAIL пользователя c авторизацией")
    public void editUserEmailWithAutorizationTest() {
        String NEWEMAIL = "NEW" + EMAIL;
        Response response = new UserApiMethod().editUserData(ACCESS_TOKEN , NEWEMAIL, PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        Assert.assertTrue((boolean) responseData.success);
    }

    @Test
    @DisplayName("Успешное изменение NAME пользователя c авторизацией")
    public void editUserNameWithAutorizationTest() {
        String NEWNAME = "NEW" + NAME;
        Response response = new UserApiMethod().editUserData(ACCESS_TOKEN , EMAIL, PASSWORD, NEWNAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        Assert.assertTrue((boolean) responseData.success);
    }

    @Test
    @DisplayName("Без успешное изменение EMAIL пользователя без авторизацией")
    public void editUserEmailWithOutAutorizationTest() {
        String NEWEMAIL = "NEW" + EMAIL;
        Response response = new UserApiMethod().editUserData("", NEWEMAIL, PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        Assert.assertFalse((boolean) responseData.success);
        Assert.assertEquals("You should be authorised", responseData.message);
    }

    @Test
    @DisplayName("Без успешное изменение NAME пользователя без авторизацией")
    public void editUserNameWithOutAutorizationTest() {
        String NEWNAME = "NEW" + NAME;
        Response response = new UserApiMethod().editUserData("", EMAIL, PASSWORD, NEWNAME);
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
