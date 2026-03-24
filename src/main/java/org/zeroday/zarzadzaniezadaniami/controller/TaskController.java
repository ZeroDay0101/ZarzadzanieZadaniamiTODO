package org.zeroday.zarzadzaniezadaniami.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zeroday.zarzadzaniezadaniami.dto.CreateTaskRequest;
import org.zeroday.zarzadzaniezadaniami.dto.TaskResponse;
import org.zeroday.zarzadzaniezadaniami.dto.UpdateTaskRequest;
import org.zeroday.zarzadzaniezadaniami.dto.UpdateTaskStatusRequest;
import org.zeroday.zarzadzaniezadaniami.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Utwórz zadanie")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse created = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Zastąp wszystkie zmienne obiektu Task")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Zastąp status obiektu Task")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id, @RequestBody UpdateTaskStatusRequest updateTaskStatusRequest) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, updateTaskStatusRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
