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

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

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
        // Проверяем, существует ли пользователь
        User user = userService.findByUserLogin(userLogin);

        if (user == null) {
            model.addAttribute("error", "Пользователь не найден");
            return "login";
        }


        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Неверный пароль");
            return "login"; // Возвращаем на страницу логина с ошибкой
        }


        return "redirect:/posts"; // Перенаправление на страницу постов
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser (@ModelAttribute User user, Model model) {
        try {
            user.setAvatarUrl("/image/i.webp");
            user.setRole("Пользователь");
            user.setActive(true);
            userService.registerUser (user);
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
