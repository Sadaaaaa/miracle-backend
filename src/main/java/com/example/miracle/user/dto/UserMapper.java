package com.example.miracle.user.dto;

import com.example.miracle.user.model.User;

public class UserMapper {
    public static User fromUserDto(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                null,
                null,
                userDto.getPassword(),
                null,
                false,
                null);
    }

    public static UserDto toUserDto(User user) {
//        String path = user.getUserImage().getPath() == null ? null : user.getUserImage().getPath();

        return new UserDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                null,
                user.isEnabled());
    }
}
