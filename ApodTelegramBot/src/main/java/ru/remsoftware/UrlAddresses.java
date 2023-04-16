package ru.remsoftware;

import ru.remsoftware.Bot.NasaApodBot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UrlAddresses {
    public static String getForUserDateUrl() {
        String forUserDateUrl = "https://api.nasa.gov/planetary/apod?api_key=IFOBKLiIYI71lLDuLKz4MiXuZHrpMMvrVZLcyl08&date=";
        String userDate = NasaApodBot.getUserDate();
        System.out.println(forUserDateUrl + userDate);
        return forUserDateUrl + userDate;
    }

    public static String getUrlApodRandom() {
        String ApodRandomPhoto = "https://api.nasa.gov/planetary/apod?api_key=IFOBKLiIYI71lLDuLKz4MiXuZHrpMMvrVZLcyl08&count=1";
        return ApodRandomPhoto;
    }
    public static String getTodayUrlApod() {
        Date todayDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(todayDate);
        String url = "https://api.nasa.gov/planetary/apod?api_key=IFOBKLiIYI71lLDuLKz4MiXuZHrpMMvrVZLcyl08&date=";
        String todayUrl = url + date;
        return todayUrl;
    }
}
