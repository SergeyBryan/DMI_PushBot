package org.example.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.example.handlers.enums.MainMenu;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class PushHandlerTest {
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private SendResponse sendResponse;
    @Mock
    private CallbackQuery callbackQuery;
    @Mock
    private UserService userService;
    @Mock
    private Message message;
    @Mock
    private Update update;

    @Mock
    private Chat chat;

    private PushHandler pushHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pushHandler = new PushHandler(userService);
        sendResponse = mock(SendResponse.class);
        pushHandler.setTelegramBot(telegramBot);

        when(sendResponse.isOk()).thenReturn(true);
        when(telegramBot.execute(any(DeleteMessage.class))).thenReturn(sendResponse);
        when(telegramBot.execute(any(SendPhoto.class))).thenReturn(sendResponse);
        when(callbackQuery.message()).thenReturn(message);
        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
    }

    @Test
    @DisplayName("Testing appliesTo method with callBackData is null")
    void shouldAppliesToReturnFalseWhenMessageIsNull() {
        when(message.text()).thenReturn(null);
        when(update.callbackQuery()).thenReturn(null);

        boolean result = pushHandler.appliesTo(update);
        assertFalse(result);

    }

    @Test
    @DisplayName("Testing appliesTo method when callBackData contains any correct text")
    void shouldAppliesToReturnTrueWhenCallBackDataIsCorrect() {
        long chatId = 123L;
        String text = "/" + MainMenu.HOW_TO_PUSH.getKey();

        when(message.text()).thenReturn(null);
        when(callbackQuery.data()).thenReturn(text);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(true);

        boolean callBackResult = pushHandler.appliesTo(update);
        assertTrue(callBackResult);
    }

    @Test
    @DisplayName("Testing appliesTo method when callBackData contains any correct text but user status != 0")
    void shouldAppliesToReturnFalseWhenCallBackDataIsUserStatusIsWrong() {
        long chatId = 123L;
        String text = "/" + MainMenu.HOW_TO_PUSH.getKey();

        when(message.text()).thenReturn(null);
        when(callbackQuery.data()).thenReturn(text);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(false);

        boolean callBackResult = pushHandler.appliesTo(update);
        assertFalse(callBackResult);
    }

    @Test
    @DisplayName("Testing handle method when correct data")
    void handleTestWhenPutAnyDepartmentMenu() {
        long chatId = 123L;

        when(update.message().chat().id()).thenReturn(chatId);

        pushHandler.handle(update);

        verify(telegramBot, times(1)).execute(any(DeleteMessage.class));
        verify(telegramBot, times(1)).execute(any(SendPhoto.class));

    }

    @Test
    @DisplayName("Testing handle method when message = null and callBackQuery contains chatId")
    void handleTestWhenMessageIsNull() {
        when(update.message()).thenReturn(null);

        pushHandler.handle(update);

        verify(telegramBot, times(1)).execute(any(DeleteMessage.class));
        verify(telegramBot, times(1)).execute(any(SendPhoto.class));
    }
}
