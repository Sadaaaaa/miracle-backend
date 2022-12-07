package com.example.miracle.user;

import com.example.miracle.auth.PersonValidator;
import com.example.miracle.auth.jwt.JWTUtil;
import com.example.miracle.exception.NotFoundException;
import com.example.miracle.user.dao.UserRepository;
import com.example.miracle.user.dto.UserDto;
import com.example.miracle.user.dto.UserMapper;
import com.example.miracle.user.model.User;
import com.example.miracle.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final PersonValidator personValidator;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public UserController(UserService userService, PersonValidator personValidator, JWTUtil jwtUtil, UserRepository userRepository) {
        this.userService = userService;
        this.personValidator = personValidator;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<?> createUser(@RequestParam(value = "file", required = false) MultipartFile file,
                                        @RequestParam(value = "user") String userDto) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDtoPojo = objectMapper.readValue(userDto, UserDto.class);

        UserDto userDtoReg = userService.createUser(userDtoPojo, file);
//        String token = jwtUtil.generateToken(userDtoReg.getUsername());

        return ResponseEntity.ok(userDtoReg);
    }

    @PatchMapping("/user")
    public ResponseEntity<?> patchUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.patchUser(userDto));
    }

    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/user/{userId}")
    public  ResponseEntity<?> getUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/admin/users")
    public  ResponseEntity<?> getAllUsers(@RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(userService.getAllUsers(from, size));
    }

    @GetMapping("/admin/users/search")
    public ResponseEntity<?> searchUsers(@RequestParam(value = "text", required = false) String text,
                                         @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                         @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(userService.search(text, from, size));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserByLogin(@RequestParam(value = "email") String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User is not found!"));
        UserDto userDto = UserMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }


}
