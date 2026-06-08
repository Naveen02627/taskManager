package com.task.Anything.Service;

import com.task.Anything.Entity.Task;
import com.task.Anything.Entity.User;
import com.task.Anything.Config.ResourceNotFoundException;
import com.task.Anything.Repository.TaskRepository;
import com.task.Anything.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public Task addTask(Task task) {
        Long userId = task.getUser().getId();
        log.info("Adding new task for user id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        log.info("Task added successfully with id: {}", savedTask.getId());
        return savedTask;
    }

    public List<Task> findAllTasksOfUser(Long userId) {
        log.info("Fetching all tasks for user id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Task> tasks = user.getTasks();
        log.info("Found {} tasks for user id: {}", tasks.size(), userId);
        return tasks;
    }

    @Transactional
    public Task deleteTask(Long taskId) {
        log.info("Deleting task with id: {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        taskRepository.delete(task);
        log.info("Task deleted successfully with id: {}", taskId);
        return task;
    }

    @Transactional
    public Task updateTask(Long taskId, Task taskDetails) {
        log.info("Updating task with id: {}", taskId);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        // Only update the task description; do not allow changing the associated user
        if (taskDetails.getTask() != null && !taskDetails.getTask().isBlank()) {
            existingTask.setTask(taskDetails.getTask());
        }

        Task updatedTask = taskRepository.save(existingTask);
        log.info("Task updated successfully with id: {}", updatedTask.getId());
        return updatedTask;
    }
}