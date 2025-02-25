package com.blog.blog.controller;

import com.blog.blog.SecurityConfig;
import com.blog.blog.model.User;
import com.blog.blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    private SecurityConfig securityConfig; // Внедрение PasswordEncoder

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверные учетные данные.");
        }
        return "login"; // Возвращает страницу логина
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String userLogin,
                        @RequestParam("password") String password,
                        Model model) {

        try {
            userService.loginUser(userLogin, password);
        } catch (Exception e) {
            // Логирование ошибки
            logger.error("Ошибка при входе в систему: ", e);
            model.addAttribute("error", "Произошла ошибка при входе. Попробуйте еще раз.");
            return "login";
        }
        return "redirect:/posts"; // Перенаправление на страницу постов
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            // Проверка уникальности пользователя
            if (userService.existsByUsername(user.getUsername())) {
                model.addAttribute("error", "Пользователь с таким именем уже существует.");
                return "register";
            }




            // Регистрация пользователя
            userService.registerUser(user);
            return "redirect:/blog-center";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Возвращаемся на страницу регистрации с ошибкой
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации. Попробуйте еще раз.");
            return "register"; // Возвращаемся на страницу регистрации с ошибкой
        }
    }
}
