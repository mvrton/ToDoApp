package com.martin.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.martin.todolist.model.User;
import com.martin.todolist.service.UserService;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String index() {
        return "index"; // Página inicial
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register"; // Redirigir a la página de registro
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, 
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {
        
        // Verificar que las contraseñas coinciden
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "register"; // Redirigir a la página de registro con el error
        }
        
        // Verificar si el usuario ya existe
        if (userService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El nombre de usuario ya existe.");
            return "register"; // Redirigir a la página de registro con el error
        }
        
        try {
            // Registrar el nuevo usuario
            User user = userService.registerUser(username, password);
        
            // Mostrar mensaje de éxito
            model.addAttribute("success", "Registro exitoso. Ahora puedes iniciar sesión.");
            return "login"; // Redirigir a la página de login después del registro exitoso
        } catch (Exception e) {
            model.addAttribute("error", "Error en el registro.");
            return "register"; // Redirigir a la página de registro con el error
        }
    }

    // Autenticar al usuario después de registro
    private void authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login"; // Página de inicio de sesión
    }
}
