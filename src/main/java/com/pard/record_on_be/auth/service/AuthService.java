package com.pard.record_on_be.auth.service;

import com.pard.record_on_be.auth.dto.UserLoginRequestDTO;
import com.pard.record_on_be.user.entity.User;
import com.pard.record_on_be.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public Map<String, Object> saveOrUpdateUser(UserLoginRequestDTO userLoginRequestDTO) {
        User user = userService.createOrUpdateUser(
                userLoginRequestDTO.getEmail(),
                userLoginRequestDTO.getImageUrl()
        );
        boolean isNewUser = user.getCreatedDate().equals(user.getModifiedDate()); // CreatedDate와 ModifiedDate가 동일하면 신규 유저

        Map<String, Object> result = new HashMap<>();
        result.put("user_id", user.getId());
        result.put("is_new_user", isNewUser);

        return result;
    }
}
