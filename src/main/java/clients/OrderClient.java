package clients;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Order;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.ServiceLinks.*;

public class OrderClient {
    @Step("Создание заказа")
    public Response create(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDER_CREATE_ENDPOINT);
    }

    @Step("Проверка наличия трека в ответе")
    public void assertHasTrack(Response response) {
        int track = response.then().extract().path("track");
        assertThat("Трек заказа отсутствует в ответе", track, is(notNullValue()));
    }

    @Step("Получение трека заказа")
    public int getTrack(Response response) {
        return response.then().extract().path("track");
    }

    @Step("Получение информации о заказе")
    public Response getByTrack(int track) {
        return given()
                .queryParam("t", track)
                .get(ORDER_INFO_ENDPOINT);
    }

    @Step("Получение списка заказов")
    public Map<String, Object> getList() {
        Response response = given()
                .contentType(ContentType.JSON)
                .get(ORDER_LIST_ENDPOINT);

        assertThat("Код ответа некорректный", response.getStatusCode(), is(200));
        return response.as(Map.class);
    }

    @Step("Отмена заказа")
    public void cancel(int track) {
        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("track", track)
                .put(ORDER_CANCEL_ENDPOINT);

        assertThat("Код ответа при отмене заказа некорректен", response.getStatusCode(), is(200));
    }
}