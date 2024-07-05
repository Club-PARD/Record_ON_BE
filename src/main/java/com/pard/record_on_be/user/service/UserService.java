package com.pard.record_on_be.user.service;

import com.pard.record_on_be.user.dto.UserDTO;
import com.pard.record_on_be.user.entity.User;
import com.pard.record_on_be.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public Map<String, Object> registerById(UserDTO.RegisterInfo registerInfo) {
        UserDTO.Create createUser = new UserDTO.Create(userRepo.findById(registerInfo.getId()).orElseThrow());
        createUser.setName(registerInfo.getName());
        createUser.setJob(registerInfo.getJob());
        User user = userRepo.save(User.toEntity(createUser));

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

    public List<UserDTO.Read> readAll(){
        // 유저들을 리스트로 가져옴
        // stream 으로 하나씩 나눔
        return userRepo.findAll()
                .stream()
                .map(UserDTO.Read::new)
                .toList();
    }

}
