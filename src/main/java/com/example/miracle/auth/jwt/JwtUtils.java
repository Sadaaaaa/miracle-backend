package com.example.miracle.auth.jwt;

import com.example.miracle.auth.model.JwtAuthentication;
import com.example.miracle.user.model.Role;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRole(Role.ROLE_USER);
        jwtInfoToken.setFirstName(claims.get("username", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

//    private static Set<Role> getRoles(Claims claims) {
//        final List<String> roles = claims.get("roles", List.class);
//        return roles.stream()
//                .map(Role::valueOf)
//                .collect(Collectors.toSet());
//    }
}
