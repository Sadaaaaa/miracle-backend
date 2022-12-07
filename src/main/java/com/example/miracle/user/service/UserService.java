package com.example.miracle.user.service;

import com.example.miracle.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto, MultipartFile file) throws IOException;

    UserDto getUserById(Integer userId);

    Page<UserDto> getAllUsers(Integer from, Integer size);

    UserDto patchUser(UserDto userDto);

    void deleteUser(Integer userId);

    List<UserDto> search(String text, Integer from, Integer size);
}
