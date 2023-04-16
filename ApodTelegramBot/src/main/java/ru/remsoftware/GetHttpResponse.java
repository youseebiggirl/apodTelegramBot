package ru.remsoftware;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class GetHttpResponse {
    private final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public static Response GetResponseRandom() {
        Request requestRandom = new Request.Builder()
                .get()
                .url(UrlAddresses.getUrlApodRandom())
                .build();

        Response responseRandom = null;
        try {
            responseRandom = HTTP_CLIENT.newCall(requestRandom).execute();
        } catch (IOException e) {
            System.out.println("Ошибка соединения с сайтом");
            throw new RuntimeException(e);
        }
        return responseRandom;
    }
    public static Response GetResponseToday() throws Exception {
        Request requestToday = new Request.Builder()
                .get()
                .url(UrlAddresses.getTodayUrlApod())
                .build();

        Response responseToday = HTTP_CLIENT.newCall(requestToday).execute();
        return responseToday;
    }
    public static Response GetResponseForUserDate() throws Exception {
        Request requestUserDate = new Request.Builder()
                .get()
                .url(UrlAddresses.getForUserDateUrl())
                .build();

        Response responseForUserDate = HTTP_CLIENT.newCall(requestUserDate).execute();
        return responseForUserDate;
    }

}
