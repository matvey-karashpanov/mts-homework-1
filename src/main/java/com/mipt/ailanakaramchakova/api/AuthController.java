package com.mipt.ailanakaramchakova.api;

import com.mipt.ailanakaramchakova.dto.LoginRequest;
import com.mipt.ailanakaramchakova.dto.TokenResponse;
import com.mipt.ailanakaramchakova.security.InMemoryUserDetailsService;
import com.mipt.ailanakaramchakova.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final InMemoryUserDetailsService inMemoryUserDetailsService;
    private final JwtUtils jwtUtils;

    public AuthController(InMemoryUserDetailsService inMemoryUserDetailsService,
      JwtUtils jwtUtils) {
        this.inMemoryUserDetailsService = inMemoryUserDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        boolean isValid = inMemoryUserDetailsService.checkPassword(request.username(),
          request.password());
        if (!isValid) {
            return ResponseEntity.status(401).build();
        }

        String rolesClaim = request.username().equals("reader")
          ? "USER,READ_PRIVILEGE"
          : "USER";

        String token = jwtUtils.generateAccessToken(request.username(),
          Map.of("roles", rolesClaim));
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
