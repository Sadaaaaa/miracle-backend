package com.example.miracle.auth;

import com.example.miracle.auth.dto.AuthenticationDto;
import com.example.miracle.auth.jwt.JwtUtil;
import com.example.miracle.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Value("${api.url}")
    private String apiUrl;

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDto authenticationDto) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDto.getUsername(),
                        authenticationDto.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        String token = jwtUtil.generateToken(authenticationDto.getUsername());
        return Map.of("jwt", token);
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<?> activate(@PathVariable String code) {
        return ResponseEntity.ok(userService.activateUser(code));
    }

    @GetMapping("/")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(apiUrl);
        return redirectView;
    }
}
