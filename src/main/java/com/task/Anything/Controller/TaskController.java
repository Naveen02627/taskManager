package com.task.Anything.Controller;

import com.task.Anything.Entity.Task;
import com.task.Anything.Service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        log.info("Received request to add a new task");
        Task savedTask = taskService.addTask(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @GetMapping("/getAll/{id}")
    public ResponseEntity<List<Task>> getAllTasks(@PathVariable Long id) {
        log.info("Received request to get all tasks for user id: {}", id);
        List<Task> tasks = taskService.findAllTasksOfUser(id);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        log.info("Received request to delete task with id: {}", id);
        Task deletedTask = taskService.deleteTask(id);
        return ResponseEntity.ok(deletedTask);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        log.info("Received request to update task with id: {}", id);
        Task updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }
}