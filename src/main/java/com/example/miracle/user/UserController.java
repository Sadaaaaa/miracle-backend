package com.example.miracle.user;

import com.example.miracle.user.dto.UserDto;
import com.example.miracle.user.service.UserServiceImpl;
import net.bytebuddy.implementation.bind.annotation.Origin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userServiceImpl.createUser(userDto));
    }


}
