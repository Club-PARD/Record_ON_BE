package com.pard.record_on_be.auth.dto;

import lombok.Getter;

@Getter
public class UserLoginRequestDTO {
    private String email;
    private String imageUrl;
}