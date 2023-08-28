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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class StockHandlerTest {
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
    @Mock
    private StockModeHandler checkStockHandler;

    private StockHandler stockHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockHandler = new StockHandler(checkStockHandler, userService);
        sendResponse = mock(SendResponse.class);
        stockHandler.setTelegramBot(telegramBot);

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
        when(update.callbackQuery()).thenReturn(null);

        boolean result = stockHandler.appliesTo(update);
        assertFalse(result);

    }

    @Test
    @DisplayName("should return true when callBackQuery contains the correct text")
    void shouldAppliesToReturnTrueWhenCallBackDataIsCorrect() {
        String text = "/Посмотреть сток";

        when(update.message()).thenReturn(null);
        when(callbackQuery.data()).thenReturn(text);

        boolean result = stockHandler.appliesTo(update);
        assertTrue(result);
    }

    @Test
    @DisplayName("should return false when data is not correct")
    void shouldAppliesToReturnFalseWhenCallBackDataIsWrong() {
        String text = "test";

        when(message.text()).thenReturn(null);
        when(callbackQuery.data()).thenReturn(text);

        boolean callBackResult = stockHandler.appliesTo(update);
        assertFalse(callBackResult);
    }

    @Test
    @DisplayName("should handle method check user status and turn on stock mode")
    void handleTestWhenPutCorrectData() {
        long chatId = 123L;
        String text = "/" + stockHandler.departmentList.get(0);

        when(update.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(text);

        stockHandler.handle(update);

        verify(telegramBot, times(1)).execute(any(DeleteMessage.class));
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(userService, times(1)).userChangeStatus(chatId, 1);
        verify(checkStockHandler, times(1)).handle(update);
    }


}
