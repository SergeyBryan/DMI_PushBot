package org.example.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.example.entity.User;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StartHandlerTest {


    @Mock
    private TelegramBot telegramBot;
    @Mock
    private SendResponse sendResponse;
    @Mock
    private UserService userService;
    @Mock
    private Message message;
    @Mock
    private CallbackQuery callbackQuery;
    @Mock
    private Update update;

    @Mock
    private Chat chat;

    private StartHandler startHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startHandler = new StartHandler(userService);
        sendResponse = mock(SendResponse.class);
        startHandler.setTelegramBot(telegramBot);

        when(sendResponse.isOk()).thenReturn(true);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(sendResponse);
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
        when(update.message().from()).thenReturn(mock(com.pengrad.telegrambot.model.User.class));
    }

    @Test
    @DisplayName("Testing appliesTo method when user send /start")
    void shouldAppliesToReturnTrueWhenUserSendStartMessage() {
        String start = "/start";

        when(message.text()).thenReturn(start);
        when(update.message()).thenReturn(message);
        when(callbackQuery.data()).thenReturn("/" + startHandler.BACK_TO_INFO);

        boolean result = startHandler.appliesTo(update);
        assertTrue(result);
    }

    @Test
    @DisplayName("Testing appliesTo method when user choose correct button")
    void shouldAppliesToReturnTrueWhenUserSendCorrectButton() {
        String test = "test";
        when(update.message()).thenReturn(message);
        when(update.message().text()).thenReturn(test);
        when(callbackQuery.data()).thenReturn("/" + startHandler.BACK_TO_INFO);
        when(update.callbackQuery()).thenReturn(callbackQuery);

        boolean callBackResult = startHandler.appliesTo(update);
        assertTrue(callBackResult);
    }

    @Test
    @DisplayName("Testing appliesTo method when user send wrong text message")
    void shouldAppliesToReturnFalseWhenMessageIsWrong() {
        when(update.message()).thenReturn(message);
        when(update.message().text()).thenReturn("/stop");

        boolean wrongMessageResult = startHandler.appliesTo(update);
        assertFalse(wrongMessageResult);
    }

    @Test
    @DisplayName("Testing handle method when data is correct")
    void handleTestWhenUserHasCorrectData() {
        long chatId = 123L;
        String firstName = "John";
        User user = new User();
        user.setStatus(1);
        user.setChatId(chatId);

        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().chat().firstName()).thenReturn(firstName);
        when(update.message().from().firstName()).thenReturn(firstName);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(true);
        when(userService.findByChatId(chatId)).thenReturn(user);

        startHandler.handle(update);
        userService.userChangeStatus(chatId, 1);

        assertEquals(userService.findByChatId(chatId).getStatus(), user.getStatus());
        verify(userService, times(1)).isUserStatusIsZero(chatId);
        verify(userService, times(2)).findByChatId(chatId);
        verify(userService, times(1)).userChangeStatus(chatId, 1);
    }

    @Test
    @DisplayName("Testing handle method when user data is null")
    void handleFalse() {
        long chatId = 123L;
        User user = null;

        when(userService.isUserStatusIsZero(chatId)).thenReturn(true);
        when(userService.findByChatId(chatId)).thenReturn(user);

        startHandler.handle(update);

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Testing handle method when user data is null and user status is wrong")
    void handleTestWhenUserDataIsNull() {
        long chatId = 123L;

        when(update.message().chat().id()).thenReturn(chatId);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(false);
        when(userService.findByChatId(chatId)).thenReturn(null);

        startHandler.handle(update);
        userService.userChangeStatus(chatId, 1);

        assertNull(userService.findByChatId(chatId));
        verify(userService, times(1)).isUserStatusIsZero(chatId);
        verify(userService, times(2)).findByChatId(chatId);
        verify(userService, times(1)).userChangeStatus(chatId, 1);
    }

}