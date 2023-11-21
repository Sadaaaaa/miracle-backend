package com.example.miracle.auth;

import com.example.miracle.auth.entity.AuthService;
import com.example.miracle.auth.entity.JwtAuthentication;
import com.example.miracle.auth.entity.JwtRequest;
import com.example.miracle.auth.entity.JwtResponse;
import com.example.miracle.auth.entity.RefreshJwtRequest;
import com.example.miracle.auth.jwt.JwtUtil;
import com.example.miracle.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.security.auth.message.AuthException;

@RestController
public class AuthController {

    private final JwtUtil jwtUtil;
//    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AuthService authService;

    @Value("${api.url}")
    private String apiUrl;

    @Autowired
    public AuthController(JwtUtil jwtUtil, UserService userService, AuthService authService) {
        this.jwtUtil = jwtUtil;
//        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.authService = authService;
    }

//    @PostMapping("/login")
//    public Map<String, String> performLogin(@RequestBody AuthenticationDto authenticationDto) {
//        UsernamePasswordAuthenticationToken authInputToken =
//                new UsernamePasswordAuthenticationToken(authenticationDto.getUsername(),
//                        authenticationDto.getPassword());
//
//        try {
//            authenticationManager.authenticate(authInputToken);
//        } catch (BadCredentialsException e) {
//            return Map.of("message", "Incorrect credentials!");
//        }
//
//        String token = jwtUtil.generateToken(authenticationDto.getUsername());
//        return Map.of("jwt", token);
//    }

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

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("hello/user")
    public ResponseEntity<String> helloUser() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello user " + authInfo.getPrincipal() + "!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("hello/admin")
    public ResponseEntity<String> helloAdmin() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello admin " + authInfo.getPrincipal() + "!");
    }


    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
