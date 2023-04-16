package ru.remsoftware.Model;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Apod {
    private int code;
    private String copyright;
    private String date;
    private String explanation;
    private String hdurl;
    private String media_type;
    private String service_version;
    private String title;
    private String url;
    private String digg_url;
    private String digg_skin;

    public Apod(int code, String copyright, String date, String explanation, String hdurl, String media_type, String service_version, String title, String url, String digg_url, String digg_skin) {
        this.code = code;
        this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.hdurl = hdurl;
        this.media_type = media_type;
        this.service_version = service_version;
        this.title = title;
        this.url = url;
        this.digg_url = digg_url;
        this.digg_skin = digg_skin;

    }

    @Override
    public String toString() {
        return "Apod data: \n{" +
                "\ncopyright='" + copyright + '\'' +
                ",\ndate='" + date + '\'' +
                ",\nexplanation='" + explanation + '\'' +
                ",\nhdurl='" + hdurl + '\'' +
                ",\nmedia_type='" + media_type + '\'' +
                ",\nservice_version='" + service_version + '\'' +
                ",\ntitle='" + title + '\'' +
                ",\nurl='" + url + '\'' +
                ",\ndigg_url='" + digg_url + '\'' +
                ",\ndigg_skin='" + digg_skin + '\'' +
                ",\ncode='" + code + '\'' +
                "\n}";
    }

    public String getCopyright() {
        return copyright;
    }

    public String  getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getHdurl() {
        return hdurl;
    }

    public String getMediaType() {
        return media_type;
    }

    public String getServiceVersion() {
        return service_version;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public int getCode() {
        return code;
    }
}


