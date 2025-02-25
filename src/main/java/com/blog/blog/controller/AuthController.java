package com.blog.blog.controller;

import com.blog.blog.model.User;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String userLogin,
                        @RequestParam("password") String password,
                        Model model) {

        try {
            userService.loginUser(userLogin, password);
        } catch (Exception e) {
            logger.error("Ошибка при входе в систему: ", e);
            model.addAttribute("error", e.getMessage());
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
            userService.registerUser(user);
            return "redirect:/blog-center";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Возвращаемся на страницу регистрации с ошибкой
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Возвращаемся на страницу регистрации с ошибкой
        }
    }
}
