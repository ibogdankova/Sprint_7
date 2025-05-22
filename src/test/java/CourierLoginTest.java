import clients.CourierClient;
import models.Courier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;
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

        Response createResponse = courierClient.create(courier);
        assertThat("Курьер не создан", createResponse.statusCode(), is(SC_CREATED));

        Response loginResponse = courierClient.login(courier);
        courierId = courierClient.getId(loginResponse);
        assertThat("ID не получен", courierId, is(notNullValue()));
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    public void loginCourierSuccess() {
        Response response = courierClient.login(courier);
        assertThat(response.statusCode(), is(SC_OK));
        assertThat(response.path("id"), is(notNullValue()));
    }

    @Test
    @DisplayName("Ошибка авторизации без логина")
    public void loginWithoutLoginShouldFail() {
        courier.setLogin("");
        Response response = courierClient.login(courier);
        assertThat(response.statusCode(), is(SC_BAD_REQUEST));
        assertThat(response.path("message"), is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка авторизации без пароля")
    public void loginWithoutPasswordShouldFail() {
        courier.setPassword("");
        Response response = courierClient.login(courier);
        assertThat(response.statusCode(), is(SC_BAD_REQUEST));
        assertThat(response.path("message"), is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным логином")
    public void loginWithWrongLoginShouldFail() {
        courier.setLogin("wrong_" + courier.getLogin());
        Response response = courierClient.login(courier);
        assertThat(response.statusCode(), is(SC_NOT_FOUND));
        assertThat(response.path("message"), is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным паролем")
    public void loginWithWrongPasswordShouldFail() {
        courier.setPassword("wrong_" + courier.getPassword());
        Response response = courierClient.login(courier);
        assertThat(response.statusCode(), is(SC_NOT_FOUND));
        assertThat(response.path("message"), is("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }
}
