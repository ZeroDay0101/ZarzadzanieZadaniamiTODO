package org.zeroday.zarzadzaniezadaniami.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zeroday.zarzadzaniezadaniami.dto.CreateTaskRequest;
import org.zeroday.zarzadzaniezadaniami.dto.TaskResponse;
import org.zeroday.zarzadzaniezadaniami.dto.UpdateTaskRequest;
import org.zeroday.zarzadzaniezadaniami.exception.TaskNotFoundException;
import org.zeroday.zarzadzaniezadaniami.model.Task;
import org.zeroday.zarzadzaniezadaniami.model.TaskStatus;
import org.zeroday.zarzadzaniezadaniami.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task();
        sampleTask.setId(1L);
        sampleTask.setTitle("Testowe zadanie");
        sampleTask.setDescription("Opis testowego zadania");
        sampleTask.setStatus(TaskStatus.NEW);
        sampleTask.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("createTask – Tworzy zadanie i zwraca odpowiedź")
    void createTask_shouldCreateAndReturnTask() {
        CreateTaskRequest request = new CreateTaskRequest("Nowe zadanie", "Opis");
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        TaskResponse response = taskService.createTask(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Testowe zadanie");
        assertThat(response.status()).isEqualTo(TaskStatus.NEW);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("getAllTasks – Zwraca listę wszystkich zadań")
    void getAllTasks_shouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(sampleTask));

        List<TaskResponse> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).title()).isEqualTo("Testowe zadanie");
    }

    @Test
    @DisplayName("getAllTasks – Zwraca pustą listę gdy brak zadań")
    void getAllTasks_shouldReturnEmptyListWhenNoTasks() {
        when(taskRepository.findAll()).thenReturn(List.of());

        List<TaskResponse> tasks = taskService.getAllTasks();

        assertThat(tasks).isEmpty();
    }

    @Test
    @DisplayName("getTaskById – Zwraca zadanie gdy istnieje")
    void getTaskById_shouldReturnTaskWhenFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        TaskResponse response = taskService.getTaskById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Testowe zadanie");
    }

    @Test
    @DisplayName("getTaskById – Rzuca wyjątek gdy zadanie nie istnieje")
    void getTaskById_shouldThrowWhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("updateTask – Aktualizuje i zwraca zadanie")
    void updateTask_shouldUpdateAndReturnTask() {
        UpdateTaskRequest request = new UpdateTaskRequest("Zaktualizowane", "Nowy opis", TaskStatus.IN_PROGRESS);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Zaktualizowane");
        updatedTask.setDescription("Nowy opis");
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);
        updatedTask.setCreatedAt(sampleTask.getCreatedAt());

        TaskResponse response = taskService.updateTask(1L, request);

        assertThat(response.title()).isEqualTo("Zaktualizowane");

        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("updateTask – Rzuca wyjątek gdy zadanie nie istnieje")
    void updateTask_shouldThrowWhenNotFound() {
        UpdateTaskRequest request = new UpdateTaskRequest("Tytuł", null, TaskStatus.DONE);
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(99L, request))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("deleteTask – Usuwa zadanie gdy istnieje")
    void deleteTask_shouldDeleteWhenFound() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteTask – Rzuca wyjątek gdy zadanie nie istnieje")
    void deleteTask_shouldThrowWhenNotFound() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> taskService.deleteTask(99L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
