package com.martin.todolist.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.martin.todolist.model.Task;
import com.martin.todolist.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void completeTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        task.setCompleted(true);
        taskRepository.save(task);
    }

    public void uncompleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        task.setCompleted(false);
        taskRepository.save(task);
    }

    public Page<Task> getTasksPage(int page, int size) {
        return taskRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public void updateTask(Long id, String newName) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        task.setName(newName);
        taskRepository.save(task);
    }
}
