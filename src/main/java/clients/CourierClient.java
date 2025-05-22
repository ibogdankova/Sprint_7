package clients;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Courier;

import static io.restassured.RestAssured.given;
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
                .contentType(ContentType.JSON)
                .when()
                .delete(COURIER_DELETE_ENDPOINT + courierId);
    }

    @Step("Получение ID курьера из ответа")
    public int getId(Response response) {
        return response.then().extract().path("id");
    }
}