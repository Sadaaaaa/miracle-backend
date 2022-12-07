package com.example.miracle.auth;

import com.example.miracle.auth.jwt.JWTUtil;
import com.example.miracle.auth.model.JwtRequest;
import com.example.miracle.auth.model.JwtResponse;
import com.example.miracle.auth.model.PersonDetails;
import com.example.miracle.auth.model.RefreshJwtRequest;
import com.example.miracle.auth.service.AuthService;
import com.example.miracle.auth.service.PersonDetailsService;
import com.example.miracle.user.dao.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
@RestController
public class AuthController {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PersonDetailsService personDetailsService;
    private final UserRepository userRepository;
    private final AuthService authService;

    public AuthController(JWTUtil jwtUtil, AuthenticationManager authenticationManager, PersonDetailsService personDetailsService, UserRepository userRepository, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.personDetailsService = personDetailsService;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @GetMapping("/showUserInfo")
    @ResponseBody
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getPerson());

        return personDetails.getUsername();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:3000/");

        return redirectView;
    }
}
