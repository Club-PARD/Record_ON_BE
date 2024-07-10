package com.pard.record_on_be.user.controller;

import com.pard.record_on_be.user.dto.UserDTO;
import com.pard.record_on_be.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/find/all")
    @Operation(summary = "모든 유저 리스팅",description = "DB 내 모든 유저 리스팅")
    public ResponseEntity<List<UserDTO.Read>> findAll() {
        List<UserDTO.Read> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/find/{userId}")
    @Operation(summary = "유저 검색",description = "ID를 통해 DB 내 해당 유저 검색")
    public UserDTO.Read readById(@PathVariable UUID userId){ return userService.readById(userId); }

    @PutMapping("/register")
    @Operation(summary = "회원가입",description = "ID를 통해 직군 설정")
    public Map<String, Object> register(@RequestBody UserDTO.RegisterInfo registerInfo){
        return userService.registerById(registerInfo);
    }
}
