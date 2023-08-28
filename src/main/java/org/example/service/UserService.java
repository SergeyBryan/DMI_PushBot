package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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

    public boolean isUserStatusIsZero(long chatId) {
        User user = userRepository.findUserByChatId(chatId);
        if (user != null) {
            return user.getStatus() == 0;
        }
        return false;
    }

    public void userChangeStatus(long chatId, int value) {
        User user = userRepository.findUserByChatId(chatId);
        if (user != null) {
            user.setStatus(value);
            userRepository.save(user);
        }
    }
}
