package DTO;


import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Order;
import org.jetbrains.annotations.NotNull;


import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.ServiceLinks.*;

public class OrderClientAPI {

        @Step("Создание заказа")
        public Response createOrder(Order order) {
            return given()
                    .contentType(ContentType.JSON)
                    .body(order)
                    .when()
                    .post(ORDER_CREATE_ENDPOINT);
        }

        @Step("Проверка, что ответ содержит трек заказа")
        public void assertOrderHasTrack(@NotNull Response response) {
            int track = response.then().extract().path("track");
            assertThat("Трек заказа отсутствует в ответе", track, is(notNullValue()));
        }

        @Step("Получение трек-номера заказа из ответа")
        public int extractOrderTrack(@NotNull Response response) {
            return response.then().extract().path("track");
        }

        @Step("Получение информации о заказе по треку")
        public Response getOrderByTrack(int track) {
            return given()
                    .queryParam("t", track)
                    .get(ORDER_INFO_ENDPOINT);
        }

        @Step("Получение списка заказов")
        public Map<String, Object> getOrderList() {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .get(ORDER_LIST_ENDPOINT);

            assertThat("Код ответа некорректный", response.getStatusCode(), is(200));
            return response.as(Map.class);
        }

        @Step("Отмена заказа по треку")
        public void cancelOrder(int track) {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .queryParam("track", track)
                    .put(ORDER_CANCEL_ENDPOINT);

            assertThat("Код ответа при отмене заказа некорректен", response.getStatusCode(), is(200));
        }

}
