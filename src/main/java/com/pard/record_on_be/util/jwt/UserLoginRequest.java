package com.pard.record_on_be.util.jwt;

import lombok.Getter;

@Getter
public class UserLoginRequest {
    private String email;
    private String name;
    private String imageUrl;
}