package com.task.Anything.Service;

import com.task.Anything.DTO.RegisterRequest;
import com.task.Anything.DTO.UserResponseDTO;
import com.task.Anything.Entity.Role;
import com.task.Anything.Entity.User;
import com.task.Anything.Config.DuplicateEmailException;
import com.task.Anything.Config.ResourceNotFoundException;
import com.task.Anything.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;



    public UserResponseDTO register(RegisterRequest registerRequest) {
        log.info("Registering new user with email: {}", registerRequest.getEmail());

        User existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser != null) {
            log.error("Email already registered: {}", registerRequest.getEmail());
            throw new DuplicateEmailException("Email " + registerRequest.getEmail() + " is already registered");
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole() != null ? registerRequest.getRole() : Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());


        return new UserResponseDTO(savedUser.getId(), savedUser.getName(),
                savedUser.getEmail(), savedUser.getRole());
    }

    public User getUser(Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponseDTO getUserDTO(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

}