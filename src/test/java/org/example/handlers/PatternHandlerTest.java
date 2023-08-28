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
import org.example.entity.Request;
import org.example.service.ModelService;
import org.example.service.RequestService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class PatternHandlerTest {

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
    private RequestService requestService;

    @Mock
    private Message message;
    @Mock
    private Update update;

    @Mock
    private Chat chat;

    private PatternHandler patternHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patternHandler = new PatternHandler(requestService, modelService, userService);
        sendResponse = mock(SendResponse.class);
        patternHandler.setTelegramBot(telegramBot);

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

        boolean result = patternHandler.appliesTo(update);
        assertFalse(result);

    }

    @Test
    @DisplayName("should return true when callBackQuery contains the correct text")
    void shouldAppliesToReturnTrueWhenCallBackDataIsCorrect() {
        String text = "test";
        long chatId = 123L;

        when(update.callbackQuery()).thenReturn(null);
        when(update.message().text()).thenReturn(text);
        when(update.message().chat().id()).thenReturn(chatId);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(true);

        assertTrue(patternHandler.appliesTo(update));
    }

    @Test
    @DisplayName("should return false when data isn't correct")
    void shouldAppliesToReturnFalseWhenCallBackDataIsUserStatusIsWrong() {
        String text = "/start";
        long chatId = 123L;

        when(update.callbackQuery()).thenReturn(null);
        when(update.message().text()).thenReturn(text);
        when(update.message().chat().id()).thenReturn(chatId);
        when(userService.isUserStatusIsZero(chatId)).thenReturn(false);

        assertFalse(patternHandler.appliesTo(update));
    }

    @Test
    @DisplayName("Testing loadAll method")
    void shouldLoadAll() {
        Integer modelCode = 8001000;
        List<Integer> list = List.of(modelCode);

        when(modelService.getModelCodes()).thenReturn(list);

        patternHandler.loadAll();

        assertEquals(patternHandler.getModels().get(0), modelCode);
        verify(modelService, times(1)).getModelCodes();
    }

    @Test
    @DisplayName("should do not find the article in the list of models")
    void handleTestWhenSendUnknownArticle() {
        long chatId = 123L;
        String modelCode = "8001000";

        when(update.message().text()).thenReturn(modelCode);
        when(update.message().chat().id()).thenReturn(chatId);

        patternHandler.handle(update);

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("should handle find model without quantity message")
    void handleTestWhenSendOnlyArticleWithoutQty() {
        long chatId = 123L;
        Integer modelCode = 800100;
        String code = "800100";
        Model model = new Model();
        model.setModelCode(modelCode);
        model.setQty(42);
        model.setModelName("Racket");

        when(modelService.getModel(modelCode)).thenReturn(model);
        when(update.message().text()).thenReturn(code);
        when(update.message().chat().id()).thenReturn(chatId);

        when(modelService.getModelCodes()).thenReturn(List.of(modelCode));
        List<Integer> list = modelService.getModelCodes();
        patternHandler.setModels(list);

        patternHandler.handle(update);
        assertTrue(patternHandler.getModels().contains(model.getModelCode()));
        verify(telegramBot, times(1)).execute(any(SendMessage.class));

    }

    @Test
    @DisplayName("should handle find model and receive text instead qty")
    void handleTestWhenSendArticleAndTextMessage() {
        long chatId = 123L;
        Integer modelCode = 800100;
        String text = "800100 test";
        Model model = new Model();
        model.setModelCode(modelCode);
        model.setQty(42);
        model.setModelName("Racket");

        when(modelService.getModel(modelCode)).thenReturn(model);
        when(update.message().text()).thenReturn(text);
        when(update.message().chat().id()).thenReturn(chatId);

        when(modelService.getModelCodes()).thenReturn(List.of(modelCode));

        List<Integer> list = modelService.getModelCodes();
        patternHandler.setModels(list);

        patternHandler.handle(update);

        assertTrue(patternHandler.getModels().contains(model.getModelCode()));
        verify(telegramBot, times(1)).execute(any(SendMessage.class));

    }

    @Test
    @DisplayName("should handle find model and receive text with more than 4 spaces")
    void handleTestWhenSendArticleAndBigAmountOfComments() {
        long chatId = 123L;
        Integer modelCode = 800100;
        String text = "800100 20 size m and l";
        Model model = new Model();
        model.setModelCode(modelCode);
        model.setQty(42);
        model.setModelName("Racket");

        when(modelService.getModel(modelCode)).thenReturn(model);
        when(update.message().text()).thenReturn(text);
        when(update.message().chat().id()).thenReturn(chatId);

        when(modelService.getModelCodes()).thenReturn(List.of(modelCode));

        List<Integer> list = modelService.getModelCodes();
        patternHandler.setModels(list);

        patternHandler.handle(update);

        assertTrue(patternHandler.getModels().contains(model.getModelCode()));
        verify(telegramBot, times(1)).execute(any(SendMessage.class));

    }

    @Test
    @DisplayName("should handle find model and create push request")
    void handleTestWhenSendCorrectMessage() {
        long chatId = 123L;
        Integer modelCode = 8001000;
        String text = "8001000 20 размер L";
        Model model = new Model();
        model.setModelCode(modelCode);
        model.setQty(42);
        model.setModelName("Racket");

        when(modelService.getModel(modelCode)).thenReturn(model);
        when(update.message().text()).thenReturn(text);
        when(update.message().chat().id()).thenReturn(chatId);

        when(modelService.getModelCodes()).thenReturn(List.of(modelCode));

        List<Integer> list = modelService.getModelCodes();
        patternHandler.setModels(list);

        patternHandler.handle(update);

        assertTrue(patternHandler.getModels().contains(model.getModelCode()));
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(requestService, times(1)).create(any(Request.class));

    }

}
