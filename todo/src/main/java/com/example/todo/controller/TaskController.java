package com.example.todo.controller;

import com.example.todo.entity.Task;
import com.example.todo.repository.TaskRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  @Autowired private TaskRepository taskRepository;

  @PostMapping
  public Task createTask(@RequestBody Task task) {
    return taskRepository.save(task);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
    // The reason we use an Optional is so that we can check if the task exists in the database.
    Optional<Task> taskOptional = taskRepository.findById(id);
    if (taskOptional.isPresent()) {
      // We get the task and use the ResponseEntity.ok() method to return a 200 OK status code with
      // the task.
      return ResponseEntity.ok(taskOptional.get());
    } else {
      // If the task does not exist, we return a 404 Not Found status code.
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
    Optional<Task> taskOptional = taskRepository.findById(id);
    if (taskOptional.isPresent()) {
      Task taskToUpdate = taskOptional.get();
      taskToUpdate.setTitle(task.getTitle());
      taskToUpdate.setDescription(task.getDescription());
      taskToUpdate.setCompleted(task.isCompleted());
      return ResponseEntity.ok(taskRepository.save(taskToUpdate));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    Optional<Task> taskOptional = taskRepository.findById(id);
    if (taskOptional.isPresent()) {
      taskRepository.deleteById(id);
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
