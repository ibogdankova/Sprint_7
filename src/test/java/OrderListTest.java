import clients.CourierClient;
import clients.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.Order;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderListTest {

    private OrderClient orderClient;
    private CourierClient courierClient;
    private Courier courier;
    private Order order;
    private int orderTrack = 0;
    private int courierId = 0;
    private final Faker faker = new Faker();

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        courierClient = new CourierClient();

        // Создание курьера
        courier = Courier.builder()
                .login(faker.name().username())
                .password(faker.internet().password())
                .firstName(faker.name().firstName())
                .build();

        Response createCourier = courierClient.create(courier);
        assertThat("Курьер не создан", createCourier.statusCode(), is(SC_CREATED));

        courierId = courierClient.getId(courierClient.login(courier));
        assertThat("ID курьера не получен", courierId, greaterThan(0));

        // Создание заказа
        order = Order.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .address(faker.address().fullAddress())
                .metroStation(faker.number().numberBetween(1, 10))
                .phone(faker.phoneNumber().phoneNumber())
                .rentTime(faker.number().numberBetween(1, 5))
                .deliveryDate(LocalDate.now().plusDays(2).toString())
                .comment("Тест заказ: " + faker.lorem().sentence())
                .color(Arrays.asList("BLACK"))
                .build();

        Response orderResponse = orderClient.create(order);
        assertThat("Ошибка при создании заказа", orderResponse.statusCode(), is(SC_CREATED));

        orderTrack = orderClient.getTrack(orderResponse);
        assertThat("Трек заказа пустой", orderTrack, greaterThan(0));
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void testGetOrderList() {
        Map<String, Object> ordersMap = orderClient.getList();

        assertThat("Список заказов не получен", ordersMap, is(notNullValue()));
        assertThat("Поле 'orders' отсутствует в ответе", ordersMap.containsKey("orders"), is(true));

        List<?> orders = (List<?>) ordersMap.get("orders");
        assertThat("Список заказов пуст", orders, is(not(empty())));
    }

    @After
    public void tearDown() {
        if (orderTrack > 0) {
            orderClient.cancel(orderTrack);
        }
        if (courierId > 0) {
            courierClient.delete(courierId);
        }
    }
}
