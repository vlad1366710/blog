package com.blog.blog.controller;

import com.blog.blog.model.User;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String adminPanel(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, является ли пользователь администратором
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUserLogin = authentication.getName();
            User currentUser  = userService.findByUserLogin(currentUserLogin);

            if (currentUser  != null && currentUser.isAdmin()) {
                // Получаем пользователей из сервиса
                List<User> users = userService.getUsers(page);
                int totalUsers = userService.getTotalUsers();
                int totalPages = (int) Math.ceil((double) totalUsers / UserService.USERS_PER_PAGE); // Предположим, что у вас есть константа для количества пользователей на странице

                model.addAttribute("users", users);
                model.addAttribute("page", page);
                model.addAttribute("total_pages", totalPages);

                return "adminPanel"; // Возвращает страницу администрирования
            }
        }

        // Если пользователь не администратор, добавляем сообщение об ошибке
        model.addAttribute("error", "У вас нет прав доступа к этой панели.");
        return "error"; // Возвращает страницу с ошибкой
    }


    @GetMapping("/admin/users/{id}/delete")
    public String deleteUser (@PathVariable Long id) {

            userService.deleteUser (id);
            return "adminPanel"; // Возвращаем статус 204 No Content

    }
}



