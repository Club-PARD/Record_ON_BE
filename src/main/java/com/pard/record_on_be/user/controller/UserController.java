package com.pard.record_on_be.user.controller;

import com.pard.record_on_be.user.dto.UserDTO;
import com.pard.record_on_be.user.servise.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/find/all")
    @Operation(summary = "모든 유저 리스팅",description = "DB 내 모든 유저 리스팅")
    public List<UserDTO.Read> readAll(){
        return userService.readAll();
    }

    @GetMapping("/find/{userId}")
    @Operation(summary = "유저 검색",description = "ID를 통해 DB 내 해당 유저 검색")
    public UserDTO.Read readById(@PathVariable UUID userId){ return userService.readById(userId); }
}
