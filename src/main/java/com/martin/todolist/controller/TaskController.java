package com.martin.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.martin.todolist.model.Task;
import com.martin.todolist.service.TaskService;

@Controller
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Obtener tareas del usuario autenticado
    @GetMapping("/tasks/view")
    public String getTasks(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Task> taskPage = taskService.getTasksPage(page, 5);
        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        return "tasks";
    }

    // Crear tarea
    @PostMapping("/tasks")
    public String createTask(@RequestParam String name) {
        Task task = new Task();
        task.setName(name);
        task.setCompleted(false);
        taskService.createTask(task);

        return "redirect:/tasks/view";
    }

    // Marcar tarea como completada
    @GetMapping("/tasks/complete/{id}")
    public String completeTask(@PathVariable Long id) {
        taskService.completeTask(id);
        return "redirect:/tasks/view";
    }

    // Marcar tarea como no completada
    @GetMapping("/tasks/uncomplete/{id}")
    public String uncompleteTask(@PathVariable Long id) {
        taskService.uncompleteTask(id);
        return "redirect:/tasks/view";
    }

    // Eliminar tarea
    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id, @RequestParam(defaultValue = "0") int page) {
        taskService.deleteTask(id);

        int remainingTasks = taskService.getTasksPage(page, 5).getNumberOfElements();
        if (remainingTasks == 0 && page > 0) {
            page--;
        }

        return "redirect:/tasks/view?page=" + page;
    }

    // Editar tarea
    @GetMapping("/tasks/edit/{id}")
    public String editTaskForm(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, Model model) {
        Task task = taskService.getTaskById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada con ID: " + id));
        model.addAttribute("task", task);
        model.addAttribute("currentPage", page);
        return "edit_task";
    }

    // Actualizar tarea
    @PostMapping("/tasks/update")
    public String updateTask(@RequestParam Long id, @RequestParam String name, @RequestParam(defaultValue = "0") int page) {
            taskService.updateTask(id, name);
            return "redirect:/tasks/view?page=" + page;
    }
}
