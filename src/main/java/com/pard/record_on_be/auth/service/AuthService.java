package com.pard.record_on_be.auth.service;

import com.pard.record_on_be.user.entity.User;
import com.pard.record_on_be.user.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepo userRepository;

    public AuthService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public User saveOrUpdateUser(String email, String name, String picture) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.update(name);
            return userRepository.save(user);
        } else {
            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .picture(picture)
                    .job("Unspecified")
                    .build();
            return userRepository.save(newUser);
        }
    }
}
