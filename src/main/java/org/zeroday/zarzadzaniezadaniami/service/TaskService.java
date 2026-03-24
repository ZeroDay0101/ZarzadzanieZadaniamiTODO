package org.zeroday.zarzadzaniezadaniami.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeroday.zarzadzaniezadaniami.dto.CreateTaskRequest;
import org.zeroday.zarzadzaniezadaniami.dto.TaskResponse;
import org.zeroday.zarzadzaniezadaniami.dto.UpdateTaskRequest;
import org.zeroday.zarzadzaniezadaniami.dto.UpdateTaskStatusRequest;
import org.zeroday.zarzadzaniezadaniami.exception.TaskNotFoundException;
import org.zeroday.zarzadzaniezadaniami.model.Task;
import org.zeroday.zarzadzaniezadaniami.model.TaskStatus;
import org.zeroday.zarzadzaniezadaniami.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(TaskStatus.NEW);

        Task saved = taskRepository.save(task);

        return TaskResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskResponse.from(task);
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());

        return TaskResponse.from(task);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    public TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setStatus(status.taskStatus());

        return TaskResponse.from(task);
    }
}
