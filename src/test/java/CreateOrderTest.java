
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.api.utils.OrderApiMethod;
import org.example.api.models.ServerResponseModel;
import org.example.api.utils.TestDataGenerator;
import org.example.api.utils.UserApiMethod;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CreateOrderTest {

    String accessToken = null;
    // Данные пользователя
    private String email;
    private String password;
    private String name;
    private String ingredient1;
    private String ingredient2;
    private String ingredient_notvalid;

    @Before
    public void setUp() {
        // Назначение значений аргументам
        email = TestDataGenerator.generateRandomEmail();
        password = TestDataGenerator.generateRandomPassword();
        name = TestDataGenerator.generateRandomName();

        ingredient1 = "61c0c5a71d1f82001bdaaa6d"; //"Флюоресцентная булка R2-D3"
        ingredient2 = "61c0c5a71d1f82001bdaaa6f"; //"Мясо бессмертных моллюсков Protostomia"
        ingredient_notvalid = "xxxxxxxxxxxxxxxxxxxxxxxx"; //невалидный хеш ингредиента
        new UserApiMethod().createUser(email, password, name); //создаем нового пользователя
        Response response = new UserApiMethod().loginUser(email, password, name); //авторизация пользователя
        var responseData = response.as(ServerResponseModel.class);
        accessToken = responseData.getAccessToken();
    }

    @Test
    @DisplayName("Проверка создания  заказа  с авторизацией")
    @Description("Отправляем API запрос с валидными данными, в полученном ответе проверяем поле success и статус код")
    public void createOrderWithAuthorizationTest() {
        Response response = new OrderApiMethod().CreateOrder(accessToken, ingredient1, ingredient2); //создаем заказ пользователя
        var responseData = response.as(ServerResponseModel.class);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        Assert.assertTrue(responseData.isSuccess());
    }

    @Test
    @DisplayName("Проверка создания  заказа  без авторизации")
    @Description("Отправляем API запрос с валидными данными ,но без авторизации, в полученном ответе проверяем поле success и статус код")
    public void createOrderWithoutAuthorizationTest() {
        Response response = new OrderApiMethod().CreateOrder("", ingredient1, ingredient2); //создаем заказ пользователя
        var responseData = response.as(ServerResponseModel.class);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        Assert.assertTrue(responseData.isSuccess());
    }

    @Test
    @DisplayName("Проверка создания  заказа с неверным хешем ингредиентов")
    @Description("Отправляем API запрос с неверным хешем ингредиентов, в полученном ответе проверяем статус код")
    public void createOrderWithNotValidIngredientsTest() {
        Response response = new OrderApiMethod().CreateOrder(accessToken, ingredient_notvalid); //создаем заказ пользователя
        Assert.assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Проверка создания  заказа  без ингредиентов")
    @Description("Отправляем API запрос без ингредиентов, в полученном ответе проверяем поле message и статус код")
    public void createOrderWithoutIngredientsTest() {
        Response response = new OrderApiMethod().CreateOrder(accessToken); //создаем заказ пользователя
        var responseData = response.as(ServerResponseModel.class);
        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Ingredient ids must be provided", responseData.getMessage());
    }

    @After
    public void tearDown() {
        accessToken = new UserApiMethod().deleteUser(accessToken); //удаляем пользователя
    }
}

