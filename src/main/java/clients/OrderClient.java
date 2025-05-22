package clients;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Order;

import java.util.Map;

import static io.restassured.RestAssured.given;
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

       @Step("Извлечение трека заказа из ответа")
    public int getTrack(Response response) {
        return response.then().extract().path("track");
    }



    @Step("Получение списка заказов")
    public Map getList() {
        return given()
                .when()
                .get(ORDER_LIST_ENDPOINT)
                .then()
                .extract()
                .as(Map.class);
    }

    @Step("Отмена заказа")
    public Response cancel(int track) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("track", track)
                .when()
                .put(ORDER_CANCEL_ENDPOINT);
    }
}