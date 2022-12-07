package com.example.miracle.user.service;

import com.example.miracle.exception.NotFoundException;
import com.example.miracle.image.model.Image;
import com.example.miracle.image.model.ImageMapper;
import com.example.miracle.user.dao.UserRepository;
import com.example.miracle.user.dto.UserDto;
import com.example.miracle.user.dto.UserMapper;
import com.example.miracle.user.model.Role;
import com.example.miracle.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto, MultipartFile file) throws IOException {
        User user = UserMapper.fromUserDto(userDto);
        user.setActive(true);
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

//        Image image;
        if (file != null) {
            Image image = ImageMapper.toImageUser(file);
            ImageMapper.linkImageToUser(image, user);
        }

        User savedUser = userRepository.save(user);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public Page<UserDto> getAllUsers(Integer from, Integer size) {
        return userRepository.findAll(PageRequest.of(from / size, size))
                .map(UserMapper::toUserDto);
    }

    @Override
    public UserDto patchUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new NotFoundException("User not found!"));
        if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        return UserMapper.toUserDto(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> search(String text, Integer from, Integer size) {
        return userRepository.search(text, PageRequest.of(from / size, size))
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
