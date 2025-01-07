package com.martin.todolist.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.martin.todolist.model.Task;
import com.martin.todolist.model.User;
import com.martin.todolist.repository.TaskRepository;
import com.martin.todolist.service.UserService;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    // Método para obtener tarea por ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // Crear tarea asociada al usuario autenticado
    public Task createTask(Task task) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        task.setUser(user); // Asociar tarea con el usuario
        return taskRepository.save(task);
    }

    // Obtener tareas del usuario autenticado
    public Page<Task> getTasksPage(int page, int size) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return taskRepository.findByUser(user, PageRequest.of(page, size));
    }

    // Marcar tarea como completada
    public void completeTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        task.setCompleted(true);
        taskRepository.save(task);
    }

    // Marcar tarea como no completada
    public void uncompleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        task.setCompleted(false);
        taskRepository.save(task);
    }

    // Eliminar tarea
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    // Actualizar tarea
    public void updateTask(Long id, String newName) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        task.setName(newName);
        taskRepository.save(task);
    }
}
