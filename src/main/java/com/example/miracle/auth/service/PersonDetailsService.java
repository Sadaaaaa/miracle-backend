package com.example.miracle.auth.service;

import com.example.miracle.auth.model.PersonDetails;
import com.example.miracle.exception.NotFoundException;
import com.example.miracle.user.dao.UserRepository;
import com.example.miracle.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public PersonDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> person = userRepository.findByEmail(email);

        if (person.isEmpty())
            throw new NotFoundException("User not found!");

        return new PersonDetails(person.get());
    }
}
