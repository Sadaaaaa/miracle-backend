package com.example.miracle.auth;


import com.example.miracle.auth.service.PersonDetailsService;
import com.example.miracle.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        try {
            personDetailsService.loadUserByUsername(user.getUsername());

        } catch (UsernameNotFoundException e) {
            return;
        }

        errors.rejectValue("username", "", "человек с таким именем пользователя уже существует");
    }
}
