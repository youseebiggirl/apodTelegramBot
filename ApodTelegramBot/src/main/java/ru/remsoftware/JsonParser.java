package ru.remsoftware;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import okhttp3.Response;
import ru.remsoftware.Model.Apod;

import java.io.Reader;

public class JsonParser {
    public static String errorMessage;
    private static final Gson GSON = new Gson();
    public Apod parseRandom() {
        try(Response response = GetHttpResponse.GetResponseRandom()) {
            Reader apodData = response.body().charStream();
            JsonArray apodArray = GSON.fromJson(apodData, JsonArray.class);
            for (JsonElement element : apodArray) {
                Apod apod = GSON.fromJson(element, Apod.class);
                return apod;
            }
        } catch (Exception e) {
            System.out.println("Ошибка парсинга");
            throw new RuntimeException(e);
        }
        return null;
    }
    public Apod parseToday() {
        try(Response response = GetHttpResponse.GetResponseToday()) {
            Reader apodData = response.body().charStream();
            Apod apod = GSON.fromJson(apodData, Apod.class);
            return apod;
        } catch (Exception e) {
            System.out.println("Ошибка парсинга");
            throw new RuntimeException(e);
        }
    }
    public Apod parseUserDate() {
        try(Response response = GetHttpResponse.GetResponseForUserDate()) {
            Reader apodData = response.body().charStream();
            Apod apod = GSON.fromJson(apodData, Apod.class);
            return apod;
        } catch (Exception e) {
            System.out.println("Ошибка парсинга");
            throw new RuntimeException(e);
        }
    }

}
