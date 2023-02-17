package com.example.miracle.auth.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AuthenticationDto {
    @NotEmpty(message = "Username should not be empty.")
    @Size(min = 2, max = 100, message = "Username should be from 2 to 100 symbols.")
    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}