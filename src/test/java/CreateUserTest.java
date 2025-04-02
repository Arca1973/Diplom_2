
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.api.ServerResponseModel;
import org.example.api.UserApiMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;

@RunWith(JUnit4.class)
public class CreateUserTest {
    int statusCode = 0;
    String ACCESS_TOKEN  = null;
    // Данные пользователя
    private String EMAIL;
    private String PASSWORD;
    private String NAME;

    @Before
    public void setUp() {
        // Назначение значений аргументам
        EMAIL = "ninja" + (int) (Math.random() * 1000000) + "@yandex.ru";
        PASSWORD = "1234" + (int) (Math.random() * 1000000);
        NAME = "saske" + (int) (Math.random() * 1000000);
    }

    @Test
    @DisplayName("Успешное создание пользователя при вводе валидных данных")
    public void validDataUserCreationTest() {
        Response response = new UserApiMethod().createUser(EMAIL, PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        ACCESS_TOKEN  = responseData.accessToken;
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        Assert.assertTrue((boolean) responseData.success);
    }

    @Test
    @DisplayName("Безуспешная попытка создания пользователя без EMAIL")
    public void withoutEmailUserCreationTest() {
        Response response = new UserApiMethod().createUser("", PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        ACCESS_TOKEN  = responseData.accessToken;
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, statusCode);
        Assert.assertFalse((boolean) responseData.success);
        Assert.assertEquals("Email, password and name are required fields", responseData.message);
    }

    @Test
    @DisplayName("Безуспешная попытка создания пользователя без PASSWORD")
    public void withoutPasswordlUserCreationTest() {
        Response response = new UserApiMethod().createUser(EMAIL, "", NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        ACCESS_TOKEN  = responseData.accessToken;
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, statusCode);
        Assert.assertFalse((boolean) responseData.success);
        Assert.assertEquals("Email, password and name are required fields", responseData.message);
    }

    @Test
    @DisplayName("Безуспешная попытка создания пользователя без NAME")
    public void withoutNameUserCreationTest() {
        Response response = new UserApiMethod().createUser(EMAIL, PASSWORD, "");
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        ACCESS_TOKEN  = responseData.accessToken;
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, statusCode);
        Assert.assertFalse((boolean) responseData.success);
        Assert.assertEquals("Email, password and name are required fields", responseData.message);
    }

    @Test
    @DisplayName("Безуспешное создание пользователя с повторяющимся EMAIL")
    public void duplicateLoginCreationTest() {
        Response response1 = new UserApiMethod().createUser(EMAIL, PASSWORD, NAME);
        var responseData1 = response1.as(ServerResponseModel.class);
        String ACCESS_TOKEN1 = responseData1.accessToken;
        Response response = new UserApiMethod().createUser(EMAIL, PASSWORD, NAME);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        ACCESS_TOKEN  = responseData.accessToken;
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, statusCode);
        Assert.assertFalse((boolean) responseData.success);
        Assert.assertEquals("User already exists", responseData.message);

        new UserApiMethod().deleteUser(ACCESS_TOKEN1);
        new UserApiMethod().deleteUser(ACCESS_TOKEN );
    }

    @After
    public void tearDown() {
        ACCESS_TOKEN  = new UserApiMethod().deleteUser(ACCESS_TOKEN);
    }
}
