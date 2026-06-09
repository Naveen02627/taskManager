package com.task.Anything.Controller;


import com.task.Anything.DTO.TaskRequest;
import com.task.Anything.Entity.Task;
import com.task.Anything.Service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@Slf4j
public class TaskController {

    private final TaskService taskService;


    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        return taskService.getUserIdByEmail(email);
    }

    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@Valid @RequestBody TaskRequest taskRequest) {
        log.info("Received request to add a new task");
        Long userId = getCurrentUserId();
        Task savedTask = taskService.addTask(taskRequest, userId);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Task>> getAllTasks() {
        Long userId = getCurrentUserId();
        log.info("Received request to get all tasks for user id: {}", userId);
        List<Task> tasks = taskService.findAllTasksOfUser(userId);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        log.info("Received request to delete task with id: {}", id);
        Task deletedTask = taskService.deleteTask(id);
        return ResponseEntity.ok(deletedTask);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest) {
        log.info("Received request to update task with id: {}", id);
        Task updatedTask = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }
}