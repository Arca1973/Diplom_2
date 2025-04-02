
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.api.OrderApiMethod;
import org.example.api.OrdersListModel;
import org.example.api.ServerResponseModel;
import org.example.api.UserApiMethod;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GetOrderListTest {

    String ACCESSETOKEN = null;
    // Данные пользователя
    private String EMAIL;
    private String PASSWORD;
    private String NAME;
    private String ingredient1;
    private String ingredient2;


    @Before
    public void setUp() {
        EMAIL = "ninja" + (int) (Math.random() * 1000000) + "@yandex.ru";
        PASSWORD = "1234" + (int) (Math.random() * 1000000);
        NAME = "saske" + (int) (Math.random() * 1000000);
        ingredient1 = "61c0c5a71d1f82001bdaaa6d"; //"Флюоресцентная булка R2-D3"
        ingredient2 = "61c0c5a71d1f82001bdaaa6f"; //"Мясо бессмертных моллюсков Protostomia"
        new UserApiMethod().createUser(EMAIL, PASSWORD, NAME); //создаем нового пользователя
        Response response = new UserApiMethod().loginUser(EMAIL, PASSWORD, NAME); //авторизация пользователя
        var responseData = response.as(ServerResponseModel.class);
        ACCESSETOKEN = responseData.accessToken;
        new OrderApiMethod().CreateOrder(ACCESSETOKEN, ingredient1, ingredient2); //создаем заказ пользователя
    }

    ;


    @Test
    @DisplayName("Проверка получения списка заказов  с авторизацией")
    public void GetOrderListWithAutorizationTest() {


        Response response = new OrderApiMethod().getOrderList(ACCESSETOKEN);
        var responseData = response.as(OrdersListModel.class);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        Assert.assertFalse(responseData.getOrders().isEmpty());

    }

    @Test
    @DisplayName("Проверка получения списка заказов  без авторизацией")
    public void GetOrderListWithoutAutorizationTest() {


        Response response = new OrderApiMethod().getOrderList("");
        var responseData = response.as(ServerResponseModel.class);
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusCode());
        Assert.assertEquals("You should be authorised", responseData.message);

    }

    @After
    public void tearDown() {
        ACCESSETOKEN = new UserApiMethod().deleteUser(ACCESSETOKEN); //удаляем пользователя
    }

}

