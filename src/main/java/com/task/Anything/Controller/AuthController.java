package com.task.Anything.Controller;


import com.task.Anything.DTO.AuthResponse;
import com.task.Anything.DTO.LoginRequest;
import com.task.Anything.SecurityConfig.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request for email: {}", loginRequest.getEmail());
        AuthResponse response = authService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }
}