import clients.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderCreatedTest {
    private OrderClient orderClient;
    private Order order;
    private int orderTrack;
    private Faker faker;

    private final List<String> color;
    private final int expectedStatus;

    public OrderCreatedTest(List<String> color, int expectedStatus) {
        this.color = color;
        this.expectedStatus = expectedStatus;
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Object[][] data() {
        return new Object[][]{
                {Arrays.asList("BLACK"), SC_CREATED},
                {Arrays.asList("GREY"), SC_CREATED},
                {Arrays.asList("BLACK", "GREY"), SC_CREATED},
                {null, SC_CREATED}
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        faker = new Faker();

        order = new Order(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().fullAddress(),
                faker.number().numberBetween(1, 10),
                faker.phoneNumber().phoneNumber(),
                faker.number().numberBetween(1, 7),
                LocalDate.now().plusDays(3).toString(),
                faker.lorem().sentence(),
                color != null ? color : null
        );
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void createOrderWithDifferentColors() {
        Response response = orderClient.create(order);
        assertThat("Некорректный код ответа при создании заказа", response.getStatusCode(), is(expectedStatus));

        orderClient.getTrack(response);
        orderTrack = orderClient.getTrack(response);

        assertThat("Track заказа должен быть валидным", orderTrack, is(notNullValue()));
    }

    @After
    public void tearDown() {
        if (orderTrack != 0) {
            orderClient.cancel(orderTrack);
        }
    }
}
