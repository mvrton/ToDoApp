package com.martin.todolist.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;  // Asegúrate de importar esta clase
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.martin.todolist.model.Task;
import com.martin.todolist.service.TaskService;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;


    @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    public void testGetAllTasks() throws Exception {

        Task task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setCompleted(true);

        // Crear un Page<Task> con la tarea de ejemplo
        Page<Task> taskPage = new PageImpl<>(List.of(task), PageRequest.of(0, 5), 1);

        // Simular el comportamiento de getAllTasks() con los parámetros page y size
        when(taskService.getTasksPage(0, 5)).thenReturn(taskPage);

        // Realizar la solicitud GET y verificar el estado 200 OK
        mockMvc.perform(get("/tasks/view"))
            .andExpect(status().isOk()); // Verifica que el código de estado sea 200 (OK)
    }
}
