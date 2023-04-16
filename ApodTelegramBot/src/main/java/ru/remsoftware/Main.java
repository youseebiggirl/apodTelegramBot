package ru.remsoftware;

import ru.remsoftware.Bot.NasaApodBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {


    public static void main(String[] args) throws Exception {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new NasaApodBot());

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
