package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.entity.Model;
import org.example.messenger.Messenger;
import org.example.service.ModelService;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * StockModel Handler class for handling searching stock requests.
 * This is Spring boot component and extends from AbstractHandler.
 */
@Component
public class StockModeHandler extends AbstractHandler {

    private final Logger logger = LoggerFactory.getLogger(StockModeHandler.class);
    private final Pattern pattern = Pattern.compile("^\\d{1,7}$");

    private final ModelService modelService;
    private final UserService userService;


    public StockModeHandler(ModelService modelService, UserService userService) {
        this.modelService = modelService;
        this.userService = userService;
    }
    /**
     * Checks if this handler is applicable to the given update.
     *
     * @param update The update to check.
     * @return true if the handler is applicable, false otherwise.
     */
    @Override
    public boolean appliesTo(Update update) {
        if (update.callbackQuery() == null) {
            long chatId = update.message().chat().id();
            String message = update.message().text();
            return !userService.isUserStatusIsZero(chatId) &&
                    !message.equals("/start");
        }
        return false;
    }
    /**
     * Handles the given update.
     *
     * @param update The update to handle.
     */
    @Override
    public void handle(Update update) {
        long chatId = update.message().chat().id();
        Model model = searchResult(update);

        if (model != null) {
            String modelInfo = "Артикул: " + model.getModelCode() +
                    "\nНазвание: " + model.getModelName() +
                    "\nКоличество: " + model.getQty();
            Messenger.sendMessage(chatId, modelInfo, telegramBot);
        }
    }

    public Model searchResult(Update update) {
        long chatId = update.message().chat().id();
        String message = update.message().text();
        Matcher matcher = pattern.matcher(message);

        if (!matcher.matches()) {
            Messenger.sendMessage(chatId, "Не правильно введён артикул, артикул должен составлять не более 7 цифр", telegramBot);
            return null;
        }

        Model model = modelService.getModel(Integer.parseInt(message));

        if (model == null) {
            Messenger.sendMessage(chatId, "Артикул не найдён, проверьте правильность введённых данных", telegramBot);
            return null;
        }
        return model;
    }
}