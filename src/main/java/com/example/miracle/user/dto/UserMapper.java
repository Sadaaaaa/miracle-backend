package com.example.miracle.user.dto;

import com.example.miracle.user.model.User;

import java.util.HashSet;

public class UserMapper {
    public static User fromUserDto(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                null,
                null,
                userDto.getPassword(),
                true);
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                null);
    }
}
