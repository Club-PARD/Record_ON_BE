package com.pard.meojeori.user.servise;

import com.pard.meojeori.user.dto.UserDTO;
import com.pard.meojeori.user.entity.User;
import com.pard.meojeori.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

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
