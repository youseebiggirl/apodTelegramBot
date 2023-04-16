package ru.remsoftware.Bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.remsoftware.JsonParser;
import ru.remsoftware.Model.Apod;
import ru.remsoftware.SQLConfiguration.DataBaseConnector;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class NasaApodBot extends TelegramLongPollingBot {
    long lastRequest = 0L;
    public static int errorNumber;
    static String userDateUrl;

    HashMap<Long, String> mapAnswer = new HashMap<>();
    ArrayList<Long> listForUserDateUrl = new ArrayList<>();


    @Override
    public String getBotUsername () {
        return "NasaApod22Bot";
    }

    @Override
    public String getBotToken () {
        String ApodBotKey = System.getenv("BOT_TOKEN");
        return ApodBotKey;
    }


    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        boolean hasText = update.hasMessage() && update.getMessage().hasText();
        String firstName = update.getMessage().getChat().getFirstName();
        String userName = update.getMessage().getChat().getUserName();
        System.out.println("Сообщение от пользователя " + userName);

        //Проверка состояния пользователя для нужного ответа
        if (listForUserDateUrl.contains(chatId)) {
            errorNumber = 0;
            lastRequest = System.currentTimeMillis();
            String message = update.getMessage().getText();
            mapAnswer.put(chatId, message);
            userDateUrl = mapAnswer.get(chatId);
            System.out.println(userDateUrl);
            listForUserDateUrl.remove(chatId);
            userDatePhoto(chatId);
            mapAnswer.remove(chatId);
            if(errorNumber == 1) {
                userDatePhoto(chatId);
            }
        } else if (hasText && (System.currentTimeMillis() - lastRequest) >= 1500) {
            String messageText = update.getMessage().getText();
            switch (messageText) {
                case "/start" -> {
                    lastRequest = System.currentTimeMillis();
                    startMessage(chatId, firstName, KeyBoardSettings.initReplyKeyboard());
                }
                case "Покажи случайное фото" -> {
                    lastRequest = System.currentTimeMillis();
                    errorNumber = 0;
                    randomPhoto(chatId);
                    if (errorNumber == 1) {
                        randomPhoto(chatId);
                    }
                }
                case "Покажи сегодняшнее фото" -> {
                    lastRequest = System.currentTimeMillis();
                    errorNumber = 0;
                    todayPhoto(chatId);
                    if (errorNumber == 1) {
                        todayPhoto(chatId);
                    }
                }
                case "Покажи фото за определённое число" -> {
                    lastRequest = System.currentTimeMillis();
                    errorNumber = 0;
                    if (!listForUserDateUrl.contains(chatId)) {
                        listForUserDateUrl.add(chatId);
                        sendMessage(chatId, "Введите дату в формате ГГГГ-ММ-ДД");
                    } else {
                        sendMessage(chatId, "Введите дату в формате ГГГГ-ММ-ДД");
                    }

                }

            }
        } else {
            sendMessage(chatId, "Не так быстро!");
        }
    }


    private void startMessage(long chatId, String name, ReplyKeyboardMarkup replyKeyboard) {
        String welcome = "Привет " + name + "!" + "\n" +  "Этот бот может отправить астрономическое фото дня." + "\n" +
                "Фото берётся с официального сайта NASA, где каждый день " + "\n" +
                "выкладывают фотогрифию нашей вселенной!";
        sendMessage(chatId, welcome);
    }

    private void todayPhoto(long chatId) {
        JsonParser parser = new JsonParser();
        Apod apod = parser.parseToday();
        //Если размер фото превышает допустимое значение или неправильного формата
        if (errorNumber == 1) {
            String capt = "Дата: "+ apod.getDate() + "\n" + "Название: " + apod.getTitle();
            sendPhoto(chatId, apod.getUrl(), capt);
            String explanation = "Объяснение: " + apod.getExplanation();
            sendMessage(chatId, explanation);
        } else if(errorNumber == 0) {
            String caption = "Дата: "+ apod.getDate() + "\n" + "Название: " + apod.getTitle() + "\n" + "Объяснение:" + "\n" + apod.getExplanation();
            sendPhoto(chatId, apod.getHdurl(), caption);
        } else if (apod.getMediaType().equals("video")) {
            sendMessage(chatId, "Сегодня выложили видео, можете попробовать посмотреть сами: " +
                    apod.getUrl() + "\n" + "Дата: " + apod.getDate() + "\n" + "Название: " + apod.getTitle() + "\n" + "Объяснение:" + "\n" + apod.getExplanation());
        }
    }

    private void userDatePhoto(long chatId) {
        JsonParser parser = new JsonParser();
        Apod apod = parser.parseUserDate();
        int code = apod.getCode();
        if (code == 400 || code == 404) {
            sendMessage(chatId, "Неправильно введена дата, или фотографии с такой датой не существует" +
                    "\n" + "Первая фотография была сделана 1995-06-20");
        }
        if (apod.getMediaType().equals("video")) {
            sendMessage(chatId, "В это число выкладывали видео, можете попробовать посмотреть сами: " +
                    apod.getUrl() + "\n" + "Дата: " + apod.getDate() + "\n" + "Название: " + apod.getTitle() + "\n" + "Объяснение:" + "\n" + apod.getExplanation());
        } else if (errorNumber == 0) {
            String caption = "Дата: " + apod.getDate() + "\n" + "Название: " + apod.getTitle() + "\n" + "Объяснение:" + "\n" + apod.getExplanation();
            sendPhoto(chatId, apod.getHdurl(), caption);
        } else if (errorNumber == 1) {
            String capt = "Дата: " + apod.getDate() + "\n" + "Название: " + apod.getTitle();
            sendPhoto(chatId, apod.getUrl(), capt);
            String explanation = "Объяснение: " + apod.getExplanation();
            sendMessage(chatId, explanation);
        }

    }

    private void randomPhoto(long chatId) {
        Apod apod = apodParse();
        if (apod.getMediaType().equals("video")) {
            sendMessage(chatId, "В это число выкладывали видео, можете попробовать посмотреть сами: " +
                    apod.getUrl() + "\n" + "Дата: " + apod.getDate() + "\n" + "Название: " + apod.getTitle() + "\n" + "Объяснение:" + "\n" + apod.getExplanation());
        }
        else if (errorNumber == 1) {
            String capt = "Дата: "+ apod.getDate() + "\n" + "Название: " + apod.getTitle();
            sendPhoto(chatId, apod.getUrl(), capt);
            String explanation = "Объяснение: " + apod.getExplanation();
            sendMessage(chatId, explanation);
        } else {
            String caption = "Дата: "+ apod.getDate() + "\n" + "Название: " + apod.getTitle() + "\n" + "Объяснение:" + "\n" + apod.getExplanation();
            sendPhoto(chatId, apod.getHdurl(), caption);
        }
    }

    private void errorMessage(long chatId) {
        String error = "Ошибка: Телеграму не удалось скачать фотографию, попробуйте ещё";
        sendMessage(chatId, error);
    }
    //Метод отправки сообщения пользователю
    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.setReplyMarkup(KeyBoardSettings.initReplyKeyboard());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    //Метод отправки фоторграфии через URL
    private void sendPhoto(long chatId, String imgUrl, String caption) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        InputFile url = new InputFile(imgUrl);
        sendPhoto.setPhoto(url);
        sendPhoto.setCaption(caption);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            System.out.println("Ошибка скачивания фотограффии");
            errorNumber = 1;
        }
    }
    // Парсинг запроса со случайными данными, и внесения их в датабазу
    public static Apod apodParse() {
        JsonParser parser = new JsonParser();
        Apod apod = parser.parseRandom();
        DataBaseConnector launchDB = new DataBaseConnector();
        launchDB.connectionDB();
        try {
            Statement st = launchDB.connectionDB().createStatement();
            String title = apod.getTitle();
            String newTitle = title.replaceAll("'", "\"");
            int putDataInBase = st.executeUpdate("INSERT INTO telegram_requests(copyright, date, hdurl, media_type, service_version, title, url) VALUES ('" + apod.getCopyright() + "','" + apod.getDate() + "', '" + apod.getHdurl() + "', '" + apod.getMediaType() + "', '" + apod.getServiceVersion() + "', '" + newTitle + "', '" + apod.getUrl() + "')");
            st.close();
            launchDB.connectionDB().close();
        } catch (SQLException e) {
            System.out.println("Ошибка вставки данных");
            throw new RuntimeException(e);
        }
        return apod;
    }

    public static String getUserDate() {
        return userDateUrl;
    }
}

