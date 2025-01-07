package com.martin.todolist.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.martin.todolist.model.Task;
import com.martin.todolist.model.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Buscar tareas de un usuario con paginación
    Page<Task> findByUser(User user, PageRequest pageRequest);

    // Buscar tareas completadas por usuario
    List<Task> findByCompletedAndUser(boolean completed, User user);
}
