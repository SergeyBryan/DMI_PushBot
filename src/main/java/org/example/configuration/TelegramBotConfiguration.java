package org.example.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for creating and configuring the Telegram bot.
 */
@Configuration
public class TelegramBotConfiguration {

    /**
     * The token used to authenticate the Telegram bot.
     */
    @Value("${TELEGRAM.BOT.TOKEN}")
    private String token;

    /**
     * Creates and configures the Telegram bot.
     *
     * @return A TelegramBot instance.
     */
    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }

}
