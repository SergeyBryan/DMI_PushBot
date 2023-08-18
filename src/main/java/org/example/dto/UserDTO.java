package org.example.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import org.example.entity.User;

@Data
public class UserDTO {

    Long chatId;
    Integer status;

    private static UserDTO userToUserDTO(User entity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setStatus(entity.getStatus());
        userDTO.setChatId(entity.getChatId());
        return userDTO;
    }

    private User userDTOToUser(UserDTO dto) {
        User user = new User();
        user.setChatId(dto.getChatId());
        user.setStatus(dto.getStatus());
        return user;
    }

}
