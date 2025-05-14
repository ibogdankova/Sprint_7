import clients.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CourierСreatedTest {
    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId; // Используем Integer для null-проверки
    private Faker faker;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        faker = new Faker();
        courier = new Courier(
                faker.name().username(),
                faker.internet().password(),
                faker.name().firstName()
        );
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void createCourierSuccess() {
        Response response = courierClient.create(courier);

        assertThat(response.statusCode(), is(SC_CREATED));
        assertThat(response.path("ok"), is(true));

        Response loginResponse = courierClient.login(courier);
        courierId = loginResponse.path("id");
        assertThat(courierId, is(notNullValue()));
    }

    @Test
    @DisplayName("Ошибка при создании дубликата курьера")
    public void createDuplicateCourierShouldFail() {
        courierClient.create(courier);
        courierId = courierClient.login(courier).path("id");

        Response response = courierClient.create(courier);

        assertThat(response.statusCode(), is(SC_CONFLICT));
        assertThat(response.path("message"), is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без логина")
    public void createCourierWithoutLoginShouldFail() {
        courier.setLogin("");
        Response response = courierClient.create(courier);

        assertThat(response.statusCode(), is(SC_BAD_REQUEST));
        assertThat(response.path("message"), is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без пароля")
    public void createCourierWithoutPasswordShouldFail() {
        courier.setPassword("");
        Response response = courierClient.create(courier);

        assertThat(response.statusCode(), is(SC_BAD_REQUEST));
        assertThat(response.path("message"), is("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        if (courierId != null && courierId != 0) {
            Response deleteResponse = courierClient.delete(courierId);
            assertThat(deleteResponse.statusCode(), is(SC_OK));
        }
    }
}
