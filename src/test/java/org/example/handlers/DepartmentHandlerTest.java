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

public class DepartmentHandlerTest {

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

    private DepartmentHandler departmentHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        departmentHandler = new DepartmentHandler(userService);
        sendResponse = mock(SendResponse.class);
        departmentHandler.setTelegramBot(telegramBot);

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

        boolean result = departmentHandler.appliesTo(update);
        assertFalse(result);

    }

    @Test
    @DisplayName("should return true when callBackQuery contains any correct text")
    void shouldAppliesToReturnTrueWhenCallBackDataIsCorrect() {
        long chatId = 123L;
        String text = "/" + departmentHandler.departmentList.stream().toList().get(0);

        when(message.text()).thenReturn(null);
        when(callbackQuery.data()).thenReturn(text);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(true);

        boolean callBackResult = departmentHandler.appliesTo(update);
        assertTrue(callBackResult);
    }

    @Test
    @DisplayName("should return false when data is correct but user status isn't zero")
    void shouldAppliesToReturnFalseWhenCallBackDataIsUserStatusIsWrong() {
        long chatId = 123L;
        String text = "/" + departmentHandler.departmentList.stream().toList().get(0);

        when(message.text()).thenReturn(null);
        when(callbackQuery.data()).thenReturn(text);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(false);

        boolean callBackResult = departmentHandler.appliesTo(update);
        assertFalse(callBackResult);
    }

    @Test
    @DisplayName("should handle the department menu with any department")
    void handleTestWhenPutAnyDepartmentMenu() {
        long chatId = 123L;
        String text = "/" + departmentHandler.departmentList.get(0);

        when(update.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(text);

        departmentHandler.handle(update);

        verify(telegramBot, times(1)).execute(any(DeleteMessage.class));
        verify(telegramBot, times(1)).execute(any(SendMessage.class));

    }

    @Test
    @DisplayName("should don't handle the department menu with wrong department text")
    void handleTestWhenPutWrongCallBackQuery() {
        long chatId = 123L;
        String text = "text";

        when(update.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(text);

        departmentHandler.handle(update);
        verify(telegramBot, times(0)).execute(any(DeleteMessage.class));
        verify(telegramBot, times(0)).execute(any(SendMessage.class));
    }
}
