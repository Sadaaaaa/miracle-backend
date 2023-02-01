package com.example.miracle.auth.service;

import com.example.miracle.auth.model.UserDetailsImpl;
import com.example.miracle.user.dao.UserRepository;
import com.example.miracle.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> person = userRepository.findByEmail(s);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new UserDetailsImpl(person.get());
    }
}
