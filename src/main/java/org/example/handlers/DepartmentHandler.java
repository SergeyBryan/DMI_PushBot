package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.messenger.Messenger;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Handler class for handling department-related requests.
 */
@Component
public class DepartmentHandler extends AbstractHandler {
    private final UserService userService;

    private final Logger LOG = LoggerFactory.getLogger(DepartmentHandler.class);
    /**
     * Constructs a DepartmentHandler instance with the given UserService dependency.
     *
     * @param userService The UserService instance to use for user-related operations.
     */
    public DepartmentHandler(UserService userService) {
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
            return false;
        }

        long chatId = update.callbackQuery().message().chat().id();
        String message = update.callbackQuery().data().substring(1);

        if (!userService.isUserStatusIsZero(chatId)) {
            return false;
        }

        return departmentList.contains(message);
    }

    /**
     * Handles the given update.
     *
     * @param update The update to handle.
     */
    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();
        String text = update.callbackQuery().data().substring(1);

        if (departmentMenu.containsKey(text)) {
            Messenger.editPast(chatId, telegramBot, update);
            Messenger.sendButtonMessage(chatId, departmentMenu.get(text), telegramBot,
                    Collections.singletonList(BACK_TO_SPORT)
            );
        }
    }

}
