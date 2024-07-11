package com.pard.record_on_be.user.service;

import com.pard.record_on_be.user.dto.UserDTO;
import com.pard.record_on_be.user.entity.User;
import com.pard.record_on_be.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public User createOrUpdateUser(String email, String name, String picture) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            user.update(name, picture);
        } else {
            user = User.builder()
                    .email(email)
                    .name(name)
                    .picture(picture)
                    .job("Unspecified")
                    .build();
        }
        return userRepo.save(user);
    }

    public Map<String, Object> registerById(UserDTO.RegisterInfo registerInfo) {
        User user = userRepo.findById(registerInfo.getId()).orElseThrow();
        user.update(registerInfo.getName(), user.getPicture()); // 이름 업데이트
        user.updateJob(registerInfo.getJob()); // 직업 업데이트
        userRepo.save(user);

        Map<String, Object> result = new HashMap<>();
        result.put("response", "success");
        result.put("name", user.getName());
        result.put("job", user.getJob());

        return result;
    }

    public UserDTO.Read readById(UUID userId){
        User user = userRepo.findById(userId).orElseThrow();
        return new UserDTO.Read(user);
    }

    public List<UserDTO.Read> findAll(){
        // 유저들을 리스트로 가져옴
        // stream 으로 하나씩 나눔
        return userRepo.findAll()
                .stream()
                .map(UserDTO.Read::new)
                .toList();
    }

}
