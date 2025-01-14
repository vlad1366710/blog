package com.blog.blog.controller;

import com.blog.blog.model.User;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        return "login"; // Возвращает страницу логина
    }




    @PostMapping("/login")
    public String login(@RequestParam("username") String userLogin,
                        @RequestParam("password") String password,
                        Model model) {
        // Проверяем, существует ли пользователь
        User user = userService.findByUserLogin(userLogin);

        if (user == null) {
            model.addAttribute("error", "Пользователь не найден");
            return "login"; // Возвращаем на страницу логина с ошибкой
        }

        // Проверяем пароль (здесь вы можете использовать более безопасный способ проверки пароля)
        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Неверный пароль");
            return "login"; // Возвращаем на страницу логина с ошибкой
        }

        // Если пользователь найден и пароль верный, перенаправляем на страницу постов
        return "posts"; // Или любой другой путь
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser (@ModelAttribute User user, Model model) {
        try {

            user.setAvatarUrl("/image/i.webp");// Устанавливаем новое значение поля
            user.setRole("Пользователь");
            userService.registerUser (user);
            return "blog-center"; // перенаправление на страницу успеха
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // возвращаемся на страницу регистрации с ошибкой
        }
    }

}
