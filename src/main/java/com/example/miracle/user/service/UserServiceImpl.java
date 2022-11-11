package com.example.miracle.user.service;

import com.example.miracle.user.dao.UserRepository;
import com.example.miracle.user.dto.UserDto;
import com.example.miracle.user.dto.UserMapper;
import com.example.miracle.user.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.fromUserDto(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.toUserDto(savedUser);
    }
}
