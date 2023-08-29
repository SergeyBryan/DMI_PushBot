package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * A service class that performs operations related to Users.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new User by saving it to the repository.
     *
     * @param user The User to be created.
     */
    public void create(User user) {
        userRepository.save(user);
    }

    /**
     * Finds a User by their chat ID.
     *
     * @param chatId The chat ID of the User.
     * @return The User with the specified chat ID, or null if not found.
     */
    public User findByChatId(long chatId) {
        return userRepository.findUserByChatId(chatId);
    }

    /**
     * Checks if the status of the User with the specified chat ID is zero.
     *
     * @param chatId The chat ID of the User.
     * @return true if the User's status is zero, false otherwise.
     */
    public boolean isUserStatusIsZero(long chatId) {
        User user = userRepository.findUserByChatId(chatId);
        if (user != null) {
            return user.getStatus() == 0;
        }
        return false;
    }

    /**
     * Changes the status of the User with the specified chat ID to the given value.
     *
     * @param chatId The chat ID of the User.
     * @param value  The new status value.
     */
    public void userChangeStatus(long chatId, int value) {
        User user = userRepository.findUserByChatId(chatId);
        if (user != null) {
            user.setStatus(value);
            userRepository.save(user);
        }
    }
}
