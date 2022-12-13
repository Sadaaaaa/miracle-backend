package com.example.miracle.auth.service;

import com.example.miracle.auth.jwt.JwtProvider;
import com.example.miracle.auth.model.JwtAuthentication;
import com.example.miracle.auth.model.JwtRequest;
import com.example.miracle.auth.model.JwtResponse;
import com.example.miracle.exception.NotFoundException;
import com.example.miracle.user.dao.UserRepository;
import com.example.miracle.user.dto.UserMapper;
import com.example.miracle.user.model.User;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final User user = userRepository.findByEmail(authRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (BCrypt.checkpw(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getEmail(), refreshToken);
            return new JwtResponse(accessToken, refreshToken, UserMapper.toUserDto(user));
        } else {
            throw new NotFoundException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null, UserMapper.toUserDto(user));
            }
        }
        return new JwtResponse(null, null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getEmail(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken, UserMapper.toUserDto(user));
            }
        }
        throw new NotFoundException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
