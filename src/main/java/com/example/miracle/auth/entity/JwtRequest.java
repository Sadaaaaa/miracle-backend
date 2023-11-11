package com.example.miracle.auth.entity;

import lombok.Data;

@Data
public class JwtRequest {
    private String login;
    private String email;
    private String username;
    private String password;
}
