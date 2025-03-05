package com.blog.blog.controller;

import com.blog.blog.model.User;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminPanel(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (isUserAdmin(authentication)) {
            List<User> users = userService.getUsers(page);
            int totalUsers = userService.getTotalUsers();
            int totalPages = (int) Math.ceil((double) totalUsers / UserService.USERS_PER_PAGE);

            model.addAttribute("users", users);
            model.addAttribute("page", page);
            model.addAttribute("totalPages", totalPages);

            return "adminPanel";
        }


        model.addAttribute("error", "У вас нет прав доступа к этой панели.");
        return "error";
    }

    @GetMapping("/admin/users/{id}/delete")
    public String deleteUser (@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser (id);
            redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно удален.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении пользователя. Попробуйте еще раз.");
        }

        return "redirect:/admin";
    }

    private boolean isUserAdmin(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated() &&
                userService.findByUserLogin(authentication.getName()).isAdmin();
    }
}
