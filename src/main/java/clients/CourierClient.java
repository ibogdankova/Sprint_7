package clients;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Courier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.ServiceLinks.*;

public class CourierClient {
    @Step("Создание курьера")
    public Response create(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(COURIER_CREATE_ENDPOINT);
    }

    @Step("Авторизация курьера")
    public Response login(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(COURIER_LOGIN_ENDPOINT);
    }

    @Step("Удаление курьера")
    public Response delete(int courierId) {
        return given()
                .delete(String.format(COURIER_DELETE_ENDPOINT, courierId));
    }

    @Step("Получение ID курьера")
    public int getId(Response response) {
        int courierId = response.then().extract().path("id");
        assertThat("ID курьера должен быть возвращён", courierId, is(notNullValue()));
        return courierId;
    }

    @Step("Проверка успешного создания")
    public void assertCreatedSuccessfully(Response response) {
        boolean isOk = response.then().extract().path("ok");
        assertThat("Создание курьера не успешно", isOk, is(true));
    }

    @Step("Проверка кода ответа")
    public void assertStatusCode(Response response, int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    @Step("Проверка сообщения об ошибке")
    public void assertErrorMessage(Response response, String expectedMessage) {
        String actualMessage = response.then().extract().path("message");
        assertThat("Текст ошибки не совпадает", actualMessage, is(expectedMessage));
    }
}