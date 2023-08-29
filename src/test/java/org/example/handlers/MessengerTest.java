package org.example.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.example.messenger.Messenger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class MessengerTest {

    @Test
    @DisplayName("should sendMessage method send any message and response is ok")
    public void testSendMessage() {
        TelegramBot telegramBotMock = mock(TelegramBot.class);


        SendResponse sendResponseMock = mock(SendResponse.class);
        when(sendResponseMock.isOk()).thenReturn(true);
        when(telegramBotMock.execute(Mockito.any(SendMessage.class))).thenReturn(sendResponseMock);

        Messenger.sendMessage(12345, "Test message", telegramBotMock);

        assertTrue(sendResponseMock.isOk());
        verify(telegramBotMock).execute(Mockito.any(SendMessage.class));
    }

    @Test
    @DisplayName("should sendMessage method send any button message and response is ok")
    public void sendButtonMessageTest() {
        TelegramBot telegramBot = mock(TelegramBot.class);

        long chatId = 123456789L;
        String message = "Test message";
        List<String> list = Arrays.asList("Button 1", "Button 2", "Button 3");

        SendResponse sendResponseMock = mock(SendResponse.class);
        when(sendResponseMock.isOk()).thenReturn(true);

        ArgumentCaptor<SendMessage> sendMessageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        when(telegramBot.execute(sendMessageCaptor.capture())).thenReturn(sendResponseMock);

        Messenger.sendButtonMessage(chatId, message, telegramBot, list);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

}




