package com.example.miracle.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class AuthenticationDto {
    @NotEmpty(message = "Name shouldn't be empty!")
    @Size(min = 2, max = 100, message = "Username should be from 2 to 100 symbols!")
    private String username;
    private String password;
}
