package org.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class DMI_PushBot {
    public static void main(String[] args) {
        SpringApplication.run(DMI_PushBot.class);
    }
}