package DTO;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Courier;
import org.jetbrains.annotations.NotNull;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.ServiceLinks.*;

public class CourierClientAPI {

        @Step("Создание курьера")
        public Response createCourier(Courier courier) {
            return given()
                    .contentType(ContentType.JSON)
                    .body(courier)
                    .when()
                    .post(COURIER_CREATE_ENDPOINT);
        }

        @Step("Авторизация курьера")
        public Response loginCourier(Courier courier ) {
            return given()
                    .contentType(ContentType.JSON)
                    .body(courier)
                    .when()
                    .post(COURIER_LOGIN_ENDPOINT);
        }

        @Step("Удаление курьера по ID")
        public Response deleteCourier(int courierId) {
            return given()
                    .delete(String.format(COURIER_DELETE_ENDPOINT, courierId));
        }

        @Step("Проверка, что ответ содержит id курьера")
        public int extractCourierId(@NotNull Response response) {
            int courierId = response.then().extract().path("id");
            assertThat("ID курьера должен быть возвращён", courierId, is(notNullValue()));
            return courierId;
        }

        @Step("Проверка успешного создания курьера (ok: true)")
        public void assertCourierCreated(@NotNull Response response) {
            boolean isOk = response.then().extract().path("ok");
            assertThat("Создание курьера не успешно", isOk, is(true));
        }

        @Step("Проверка кода ответа")
        public void assertStatusCode(@NotNull Response response, int expectedStatus) {
            int actualStatus = response.getStatusCode();
            assertThat("Код ответа не совпадает", actualStatus, is(expectedStatus));
        }

        @Step("Проверка сообщения об ошибке")
        public void assertErrorMessage(@NotNull Response response, String expectedMessage) {
            String actualMessage = response.then().extract().path("message");
            assertThat("Текст ошибки не совпадает", actualMessage, is(expectedMessage));
        }


}
