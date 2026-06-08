package com.task.Anything.Controller;

import com.task.Anything.Entity.User;
import com.task.Anything.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        log.info("Received request to register user with email: {}", user.getEmail());
        User registeredUser = userService.register(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam Long id) {
        log.info("Received request to get user with id: {}", id);
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }
}