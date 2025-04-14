import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.api.utils.OrderApiMethod;
import org.example.api.models.OrdersListModel;
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
public class GetOrderListTest {
    String accessToken = null;
    // Данные пользователя
    private String email;
    private String password;
    private String name;
    private String ingredient1;
    private String ingredient2;

    @Before
    public void setUp() {
        // Назначение значений аргументам
        email = TestDataGenerator.generateRandomEmail();
        password = TestDataGenerator.generateRandomPassword();
        name = TestDataGenerator.generateRandomName();

        ingredient1 = "61c0c5a71d1f82001bdaaa6d"; //"Флюоресцентная булка R2-D3"
        ingredient2 = "61c0c5a71d1f82001bdaaa6f"; //"Мясо бессмертных моллюсков Protostomia"
        new UserApiMethod().createUser(email, password, name); //создаем нового пользователя
        Response response = new UserApiMethod().loginUser(email, password, name); //авторизация пользователя
        var responseData = response.as(ServerResponseModel.class);
        accessToken = responseData.getAccessToken();
        new OrderApiMethod().CreateOrder(accessToken, ingredient1, ingredient2); //создаем заказ пользователя
    }

    @Test
    @DisplayName("Проверка получения списка заказов  с авторизацией")
    @Description("Отправляем API запрос с авторизацией, проверяем статус код и что в полученном ответе содержиться не пустой список заказов")
    public void GetOrderListWithAutorizationTest() {
        Response response = new OrderApiMethod().getOrderList(accessToken);
        var responseData = response.as(OrdersListModel.class);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        Assert.assertFalse(responseData.getOrders().isEmpty());
    }

    @Test
    @DisplayName("Проверка получения списка заказов  без авторизацией")
    @Description("Отправляем API запрос без авторизации, в полученном ответе проверяем поля message и статус код")
    public void GetOrderListWithoutAutorizationTest() {
        Response response = new OrderApiMethod().getOrderList("");
        var responseData = response.as(ServerResponseModel.class);
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusCode());
        Assert.assertEquals("You should be authorised", responseData.getMessage());
    }

    @After
    public void tearDown() {
        accessToken = new UserApiMethod().deleteUser(accessToken); //удаляем пользователя
    }
}

