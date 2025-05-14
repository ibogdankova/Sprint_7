package utils;

public class ServiceLinks {
    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    // Courier endpoints
    public static final String COURIER_CREATE_ENDPOINT = BASE_URI + "/api/v1/courier";
    public static final String COURIER_LOGIN_ENDPOINT = BASE_URI + "/api/v1/courier/login";
    public static final String COURIER_DELETE_ENDPOINT = BASE_URI + "/api/v1/courier/";

    // Order endpoints
    public static final String ORDER_CREATE_ENDPOINT = BASE_URI + "/api/v1/orders";
    public static final String ORDER_LIST_ENDPOINT = BASE_URI + "/api/v1/orders";
    public static final String ORDER_CANCEL_ENDPOINT = BASE_URI + "/api/v1/orders/cancel";

}