package com.task.Anything.Service;

import com.task.Anything.Entity.User;
import com.task.Anything.Config.DuplicateEmailException;
import com.task.Anything.Config.ResourceNotFoundException;
import com.task.Anything.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User register(User user) {
        log.info("Registering new user with email: {}", user.getEmail());

        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            log.error("Email already registered: {}", user.getEmail());
            throw new DuplicateEmailException("Email " + user.getEmail() + " is already registered");
        }

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());
        return savedUser;
    }

    public User getUser(Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}