package com.task.Anything.Controller;


import com.task.Anything.DTO.RegisterRequest;
import com.task.Anything.DTO.UserResponseDTO;
import com.task.Anything.Entity.Role;
import com.task.Anything.Entity.User;
import com.task.Anything.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // Helper method to get current authenticated user
    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        return userService.getUserByEmail(email);  // You need to implement this method in UserService
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Received request to register user with email: {}", registerRequest.getEmail());
        UserResponseDTO registeredUser = userService.register(registerRequest);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserResponseDTO> getUser(@RequestParam Long id) {
        User currentUser = getCurrentUser();
        if (!currentUser.getId().equals(id) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("You can only access your own data");
        }
        return ResponseEntity.ok(userService.getUserDTO(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("Admin request to fetch all users");
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}