package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        userRepository.save(user);
    }

    public User findByChatId(long chatId) {
        return userRepository.findUserByChatId(chatId);
    }

    public boolean isUserServiceIsZero(long chatId) {
        User user = userRepository.findUserByChatId(chatId);
        if (user == null) {
            return false;
        }
        return user.getStatus() == 0;
    }

    public void userChangeStatus(long chatId, int value) {
        User user = userRepository.findUserByChatId(chatId);
        logger.warn("Проверка на смену статуса");
        if (user != null) {
            logger.warn("меняем статус на {}", value);
            user.setStatus(value);
            userRepository.save(user);
        }
    }

}
