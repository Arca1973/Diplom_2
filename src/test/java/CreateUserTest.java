
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.api.models.ServerResponseModel;
import org.example.api.utils.TestDataGenerator;
import org.example.api.utils.UserApiMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;

@RunWith(JUnit4.class)
public class CreateUserTest {
    int statusCode = 0;
    String accessToken = null;
    // Данные пользователя
    private String email;
    private String password;
    private String name;

    @Before
    public void setUp() {
        // Назначение значений аргументам
        email = TestDataGenerator.generateRandomEmail();
        password = TestDataGenerator.generateRandomPassword();
        name = TestDataGenerator.generateRandomName();
    }

    @Test
    @DisplayName("Успешное создание пользователя при вводе валидных данных")
    @Description("Отправляем API запрос с валидными данными, в полученном ответе проверяем поле success и статус код")
    public void validDataUserCreationTest() {
        Response response = new UserApiMethod().createUser(email, password, name);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        accessToken = responseData.getAccessToken();
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        Assert.assertTrue(responseData.isSuccess());
    }

    @Test
    @DisplayName("Безуспешная попытка создания пользователя без EMAIL")
    @Description("Отправляем API запрос без EMAIL, в полученном ответе проверяем поля success, message и статус код")
    public void withoutEmailUserCreationTest() {
        Response response = new UserApiMethod().createUser("", password, name);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        accessToken = responseData.getAccessToken();
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, statusCode);
        Assert.assertFalse(responseData.isSuccess());
        Assert.assertEquals("Email, password and name are required fields", responseData.getMessage());
    }

    @Test
    @DisplayName("Безуспешная попытка создания пользователя без PASSWORD")
    @Description("Отправляем API запрос без PASSWORD, в полученном ответе проверяем поля success, message и статус код")
    public void withoutPasswordUserCreationTest() {
        Response response = new UserApiMethod().createUser(email, "", name);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        accessToken = responseData.getAccessToken();
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, statusCode);
        Assert.assertFalse(responseData.isSuccess());
        Assert.assertEquals("Email, password and name are required fields", responseData.getMessage());
    }

    @Test
    @DisplayName("Безуспешная попытка создания пользователя без NAME")
    @Description("Отправляем API запрос без NAME, в полученном ответе проверяем поля success, message и статус код")
    public void withoutNameUserCreationTest() {
        Response response = new UserApiMethod().createUser(email, password, "");
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        accessToken = responseData.getAccessToken();
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, statusCode);
        Assert.assertFalse(responseData.isSuccess());
        Assert.assertEquals("Email, password and name are required fields", responseData.getMessage());
    }

    @Test
    @DisplayName("Безуспешное создание пользователя с повторяющимся EMAIL")
    @Description("Отправляем API запрос с повторяющимся EMAIL, в полученном ответе проверяем поля success, message и статус код")
    public void duplicateLoginCreationTest() {
        Response response1 = new UserApiMethod().createUser(email, password, name);
        var responseData1 = response1.as(ServerResponseModel.class);
        String accessToken1 = responseData1.getAccessToken();
        Response response = new UserApiMethod().createUser(email, password, name);
        var responseData = response.as(ServerResponseModel.class);
        statusCode = response.getStatusCode();
        accessToken = responseData.getAccessToken();
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, statusCode);
        Assert.assertFalse(responseData.isSuccess());
        Assert.assertEquals("User already exists", responseData.getMessage());

        new UserApiMethod().deleteUser(accessToken1); //в этом тесте создается два новых пользователя, первого я удаляю в этой строке, а второго в tearDown()
            }

    @After
    public void tearDown() {
        accessToken = new UserApiMethod().deleteUser(accessToken);
    }
}
