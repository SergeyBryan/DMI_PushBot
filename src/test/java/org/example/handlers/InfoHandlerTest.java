package org.example.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
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

public class InfoHandlerTest {


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

    private InfoHandler infoHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        infoHandler = new InfoHandler(userService);
        sendResponse = mock(SendResponse.class);
        infoHandler.setTelegramBot(telegramBot);

        when(sendResponse.isOk()).thenReturn(true);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(sendResponse);
        when(telegramBot.execute(any(DeleteMessage.class))).thenReturn(sendResponse);
        when(callbackQuery.message()).thenReturn(message);
        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
    }

    @Test
    @DisplayName("Testing appliesTo method when all messages are null")
    void shouldAppliesToReturnFalseWhenMessageIsNull() {
        when(message.text()).thenReturn(null);
        when(update.callbackQuery()).thenReturn(null);

        boolean result = infoHandler.appliesTo(update);
        assertFalse(result);
    }

    @Test
    @DisplayName("Testing appliesTo method and send correct data")
    void shouldAppliesToReturnTrueWhenCallBackDataIsCorrect() {
        long chatId = 123L;

        when(message.text()).thenReturn(null);
        when(callbackQuery.data()).thenReturn("/" + infoHandler.BACK_TO_SPORT);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(true);

        boolean callBackResult = infoHandler.appliesTo(update);
        assertTrue(callBackResult);
    }

    @Test
    @DisplayName("Testing appliesTo method and send wrong button")
    void shouldAppliesToReturnFalseWhenCallBackDataContainWrongButton() {
        long chatId = 123L;

        when(message.text()).thenReturn(null);
        when(callbackQuery.data()).thenReturn("/" + infoHandler.BACK_TO_SPORT);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(false);

        boolean callBackResult = infoHandler.appliesTo(update);
        assertFalse(callBackResult);
    }

    @Test
    @DisplayName("Testing handle when send any button from infoMenu list")
    void handleTestWhenPutAnyInfoMenu() {
        long chatId = 123L;
        String text = "/" + infoHandler.infoMenu.keySet().stream().toList().get(0);

        when(update.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(text);

        infoHandler.handle(update);

        verify(telegramBot, times(1)).execute(any(DeleteMessage.class));
        verify(telegramBot, times(1)).execute(any(SendMessage.class));

    }

    @Test
    @DisplayName("Testing handle when send button will return to sport menu")
    void handleTestWhenPutBackToSportButton() {
        long chatId = 123L;
        String text = "/" + infoHandler.BACK_TO_SPORT;

        when(update.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(text);

        infoHandler.handle(update);

        verify(telegramBot, times(1)).execute(any(DeleteMessage.class));
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }
}
