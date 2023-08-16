package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.entity.Request;
import org.example.messenger.Messenger;
import org.example.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PatternHandler extends AbstractHandler {

    private final Pattern pattern = Pattern.compile("(\\d{7}) (\\d{1,4}) (\\p{L}+) ([\\p{L}\\d]{1,3})");
    private final Pattern pattern1 = Pattern.compile("(\\d{7})");

    private final Logger logger = LoggerFactory.getLogger(PatternHandler.class);

    private final RequestRepository requestRepository;

    public PatternHandler(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public boolean appliesTo(Update update) {
        if (count == 0) {
            logger.info("{}", count);
            if (update.callbackQuery() != null) {
                return false;
            }
//        return returnResult(update);
//        return update.message().text().matches(pattern.toString());
            return !update.message().text().startsWith("/start");
        }
        return false;
    }

    @Override
    public void handle(Update update) {
        if (returnResult(update)) {
            Matcher matcher = pattern.matcher(update.message().text());
            long chatId = update.message().chat().id();
            if (matcher.find()) {
                createRequest(update, matcher);
                Messenger.sendMessage(chatId, "Запрос на пуш создан", telegramBot);
            }
        }
    }

    private boolean returnResult(Update update) {
        String message = update.message().text();
        long chatId = update.message().chat().id();
        // Проверка первой части сообщения
        Pattern pattern1 = Pattern.compile("^\\d{7}$");
        Matcher matcher1 = pattern1.matcher(message.split(" ")[0]);
        if (!matcher1.matches()) {
            Messenger.sendMessage(chatId, "Не правильно введён артикул, артикул должен составлять 7 цифр. \n" +
                    "Проверьете правильность заполнения запроса", telegramBot);
            return false;
        }
        logger.info("{}", message.split(" ").length);
        if (message.split(" ").length <= 1) {
            Messenger.sendMessage(update.message().chat().id(), "В сообщении не указано количество и размер", telegramBot);
            return false;
        }

        // Проверка второй части сообщения
        Pattern pattern2 = Pattern.compile("^\\d{1,3}$");
        Matcher matcher2 = pattern2.matcher(message.split(" ")[1]);
        if (!matcher2.matches()) {
            System.out.println("Ошибка: вторая часть должна состоять из 1-3 цифр");
            Messenger.sendMessage(chatId, "Не верно указано количество, количество должно состоять от 1 до 3 цифр", telegramBot);
            return false;
        }

        // Проверка наличия третьей части сообщения
        if (message.split(" ").length < 3) {
            Messenger.sendMessage(chatId, "Вы не указали коментарий", telegramBot);
            System.out.println("Ошибка: третья часть сообщения отсутствует");
            return false;
        }
        return true;
    }


    private void createRequest(Update update, Matcher matcher) {
        if (!matcher.group(2).isBlank()) {
            logger.info("прошло");
            Request request = new Request();
            request.setModelCode(Integer.parseInt(matcher.group(1)));
            request.setQty(Integer.parseInt(matcher.group(2)));
            request.setComment(matcher.group(3));
            request.setSize(matcher.group(4));
            requestRepository.save(request);
        } else {
            Messenger.sendMessage(update.message().chat().id(), "В вашем сообщении не указано количество", telegramBot);
        }
    }

}
