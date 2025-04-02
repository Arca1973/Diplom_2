
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.api.OrderApiMethod;
import org.example.api.ServerResponseModel;
import org.example.api.UserApiMethod;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CreateOrderTest {

    String ACCESSETOKEN = null;
    // Данные пользователя
    private String EMAIL;
    private String PASSWORD;
    private String NAME;
    private String ingredient1;
    private String ingredient2;
    private String ingredient_notvalid;

    @Before
    public void setUp() {
        EMAIL = "ninja" + (int) (Math.random() * 1000000) + "@yandex.ru";
        PASSWORD = "1234" + (int) (Math.random() * 1000000);
        NAME = "saske" + (int) (Math.random() * 1000000);
        ingredient1 = "61c0c5a71d1f82001bdaaa6d"; //"Флюоресцентная булка R2-D3"
        ingredient2 = "61c0c5a71d1f82001bdaaa6f"; //"Мясо бессмертных моллюсков Protostomia"
        ingredient_notvalid = "xxxxxxxxxxxxxxxxxxxxxxxx"; //невалидный хеш ингредиента
        new UserApiMethod().createUser(EMAIL, PASSWORD, NAME); //создаем нового пользователя
        Response response = new UserApiMethod().loginUser(EMAIL, PASSWORD, NAME); //авторизация пользователя
        var responseData = response.as(ServerResponseModel.class);
        ACCESSETOKEN = responseData.accessToken;

    }

    @Test
    @DisplayName("Проверка создания  заказа  с авторизацией")
    public void createOrderWithAutorizationTest() {
        Response response = new OrderApiMethod().CreateOrder(ACCESSETOKEN, ingredient1, ingredient2); //создаем заказ пользователя
        var responseData = response.as(ServerResponseModel.class);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        Assert.assertTrue((boolean) responseData.success);
    }

    @Test
    @DisplayName("Проверка создания  заказа  без авторизации")
    public void createOrderWithoutAutorizationTest() {
        Response response = new OrderApiMethod().CreateOrder("", ingredient1, ingredient2); //создаем заказ пользователя
        var responseData = response.as(ServerResponseModel.class);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        Assert.assertTrue((boolean) responseData.success);
    }

    @Test
    @DisplayName("Проверка создания  заказа с неверным хешем ингредиентов")
    public void createOrderWithNotValidIngridietsTest() {
        Response response = new OrderApiMethod().CreateOrder(ACCESSETOKEN, ingredient_notvalid); //создаем заказ пользователя
        Assert.assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Проверка создания  заказа  без ингридиентов")
    public void createOrderWithoutIngridietsTest() {
        Response response = new OrderApiMethod().CreateOrder(ACCESSETOKEN); //создаем заказ пользователя
        var responseData = response.as(ServerResponseModel.class);
        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Ingredient ids must be provided", responseData.message);
    }

    @After
    public void tearDown() {
        ACCESSETOKEN = new UserApiMethod().deleteUser(ACCESSETOKEN); //удаляем пользователя
    }

}

