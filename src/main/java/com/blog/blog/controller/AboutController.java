package com.blog.blog.controller;

import com.blog.blog.service.AccountService;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для обработки запросов, связанных с информацией о сайте.
 */
@Controller
public class AboutController {

    private final UserService userService;
    private final AccountService accountService;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param userService    Сервис для работы с пользователями.
     * @param accountService Сервис для работы с учетными записями.
     */
    @Autowired
    public AboutController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    /**
     * Обрабатывает GET-запрос на страницу "О нас".
     *
     * @param error Параметр ошибки (опционально).
     * @param model Модель для передачи данных в представление.
     * @return Название представления "aboutUs".
     */
    @GetMapping("/aboutUs")
    public String aboutUs(@RequestParam(value = "error", required = false) String error, Model model) {
        String currentUserName = accountService.getUserName();

        // Добавляем информацию о текущем пользователе и его роли в модель
        model.addAttribute("isAdmin", userService.isAdmin(currentUserName));
        model.addAttribute("currentUser", userService.getUserInfo(currentUserName));

        // Если есть ошибка, добавляем сообщение об ошибке в модель
        if (error != null) {
            model.addAttribute("errorMessage", "An error occurred during login.");
        }

        return "aboutUs";
    }
}