package org.zeroday.zarzadzaniezadaniami.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.zeroday.zarzadzaniezadaniami.dto.CreateTaskRequest;
import org.zeroday.zarzadzaniezadaniami.dto.UpdateTaskRequest;
import org.zeroday.zarzadzaniezadaniami.model.TaskStatus;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/tasks – Tworzy zadanie i zwraca 201")
    void createTask_shouldReturn201() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Nowe zadanie", "Opis zadania");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Nowe zadanie"))
                .andExpect(jsonPath("$.description").value("Opis zadania"))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/tasks – Zwraca listę zadań")
    void getAllTasks_shouldReturnList() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Zadanie 1", null);
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Zadanie 1"));
    }

    @Test
    @DisplayName("GET /api/tasks/{id} – Zwraca zadanie po id")
    void getTaskById_shouldReturnTask() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Do pobrania", "Opis");
        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Do pobrania"));
    }

    @Test
    @DisplayName("GET /api/tasks/{id} – Zwraca 404 gdy brak zadania")
    void getTaskById_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("PUT /api/tasks/{id} – Aktualizuje zadanie")
    void updateTask_shouldUpdateAndReturn200() throws Exception {
        CreateTaskRequest createReq = new CreateTaskRequest("Oryginalne", "Opis");
        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        UpdateTaskRequest updateReq = new UpdateTaskRequest("Zaktualizowane", "Nowy opis", TaskStatus.IN_PROGRESS);

        mockMvc.perform(put("/api/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Zaktualizowane"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} – Usuwa zadanie i zwraca 204")
    void deleteTask_shouldReturn204() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Do usunięcia", null);
        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/tasks/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/tasks – Zwraca 400 gdy brak tytułu")
    void createTask_shouldReturn400WhenTitleBlank() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("", "Opis");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/tasks/{id} – Zwraca 400 gdy brak statusu")
    void updateTask_shouldReturn400WhenStatusNull() throws Exception {
        CreateTaskRequest createReq = new CreateTaskRequest("Zadanie", null);
        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        UpdateTaskRequest invalidRequest = new UpdateTaskRequest("Nowy Tytuł", "Opis", null);

        mockMvc.perform(put("/api/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
