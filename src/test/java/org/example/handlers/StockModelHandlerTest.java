package org.example.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.example.entity.Model;
import org.example.service.ModelService;
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

public class StockModelHandlerTest {

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private SendResponse sendResponse;
    @Mock
    private CallbackQuery callbackQuery;
    @Mock
    private UserService userService;
    @Mock
    private ModelService modelService;
    @Mock
    private Message message;
    @Mock
    private Update update;

    @Mock
    private Chat chat;

    private StockModeHandler stockModeHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockModeHandler = new StockModeHandler(modelService, userService);
        sendResponse = mock(SendResponse.class);
        stockModeHandler.setTelegramBot(telegramBot);

        when(sendResponse.isOk()).thenReturn(true);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(sendResponse);
        when(telegramBot.execute(any(DeleteMessage.class))).thenReturn(sendResponse);
        when(callbackQuery.message()).thenReturn(message);
        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
    }

    @Test
    @DisplayName("should return false when message is null")
    void shouldAppliesToReturnFalseWhenMessageIsNull() {
        when(message.text()).thenReturn(null);
        when(update.callbackQuery()).thenReturn(callbackQuery);

        boolean result = stockModeHandler.appliesTo(update);
        assertFalse(result);

    }

    @Test
    @DisplayName("should return true when send message is not equal /start")
    void shouldAppliesToReturnTrueWhenMessageIsCorrect() {
        String text = "test";
        long chatId = 123L;

        when(update.callbackQuery()).thenReturn(null);
        when(update.message().text()).thenReturn(text);
        when(update.message().chat().id()).thenReturn(chatId);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(false);

        assertTrue(stockModeHandler.appliesTo(update));
    }

    @Test
    @DisplayName("should return false when data is equal /start message")
    void shouldAppliesToReturnFalseWhenNotDigitTextIsProvided() {
        String text = "/start";
        long chatId = 123L;

        when(update.callbackQuery()).thenReturn(null);
        when(update.message().text()).thenReturn(text);
        when(update.message().chat().id()).thenReturn(chatId);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(false);

        assertFalse(stockModeHandler.appliesTo(update));
    }

    @Test
    @DisplayName("should handle return right model info")
    void handleTestWhenPutCorrectData() {
        long chatId = 123L;
        Integer modelCode = 8001000;
        Model model = new Model();
        model.setModelName("8001000");
        model.setQty(42);
        model.setModelName("Racket");

        when(modelService.getModel(modelCode)).thenReturn(model);
        when(update.message().text()).thenReturn("8001000");
        when(stockModeHandler.searchResult(update)).thenReturn(model);
        when(update.message().chat().id()).thenReturn(chatId);

        stockModeHandler.handle(update);

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(modelService, times(1)).getModel(modelCode);

    }

    @Test
    @DisplayName("should handle doesn't match wrong update message")
    void handleTestWhenPutMismatchMessage() {
        long chatId = 123L;

        when(update.message().text()).thenReturn("start");
        when(update.message().chat().id()).thenReturn(chatId);

        assertNull(stockModeHandler.searchResult(update));
        verify(telegramBot, times(1)).execute(any(SendMessage.class));

    }

    @Test
    @DisplayName("Should return null if model code is not found in the database")
    void handleTestSearchResultWhenModelIsNotExistInDataBase() {
        long chatId = 123L;
        Integer modelCode = 8001000;
        String code = "8001000";

        when(modelService.getModel(modelCode)).thenReturn(null);
        when(update.message().text()).thenReturn(code);
        when(update.message().chat().id()).thenReturn(chatId);

        stockModeHandler.searchResult(update);

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(modelService, times(1)).getModel(modelCode);
    }


}
