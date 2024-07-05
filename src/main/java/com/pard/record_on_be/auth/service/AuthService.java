package com.pard.record_on_be.auth.service;

import com.pard.record_on_be.user.entity.User;
import com.pard.record_on_be.user.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepo userRepository;

    public AuthService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Object> saveOrUpdateUser(String email, String name, String picture) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        Boolean isNewUser = optionalUser.isEmpty();
        User user;
        if (Boolean.FALSE.equals(isNewUser)) {
            user = optionalUser.get();
            user.update(name);

        } else {
            user = User.builder()
                    .email(email)
                    .name(name)
                    .picture(picture)
                    .job("Unspecified")
                    .build();
            userRepository.save(user);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("user_id", user.getId());
        result.put("is_new_user", isNewUser);

        return result;
    }
}
