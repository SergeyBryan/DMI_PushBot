package org.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Configuration class for configuring date-related properties.
 */
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class DateConfig {

    /**
     * Provides current date and time information.
     *
     * @return A DateTimeProvider instance.
     */
    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(LocalDate.now());
    }
}
