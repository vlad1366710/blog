package com.blog.blog.controller;

import com.blog.blog.model.User;
import com.blog.blog.service.AccountService;
import com.blog.blog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
public class AccountController {

    // Логгер для класса AccountController
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public AccountController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/account-settings")
    public String accountSettings(Model model) {
        logger.info("Обработка GET-запроса на страницу настроек аккаунта");

        String currentUserName = accountService.getUserName();
        logger.debug("Текущий пользователь: {}", currentUserName);

        model.addAttribute("currentUser", userService.getUserInfo(currentUserName));


        logger.info("Страница настроек аккаунта успешно загружена");
        return "account-settings";
    }

    @PostMapping("/upload-avatar")
    public String uploadAvatar(
            @RequestParam("avatar") MultipartFile file,
            RedirectAttributes redirectAttributes,
            Model model) { // Добавляем параметр Model
        logger.info("Обработка запроса на загрузку аватара");

        try {
            accountService.uploadAvatar(file);
            redirectAttributes.addFlashAttribute("message", "Аватар успешно загружен.");
            return "redirect:/account-settings";
        } catch (IllegalArgumentException e) {
            logger.warn("Ошибка при загрузке аватара: {}", e.getMessage());
            model.addAttribute("error", e.getMessage()); // Добавляем ошибку в модель
            return "account-settings"; // Возвращаем страницу с ошибкой
        } catch (IOException e) {
            logger.error("Ошибка при загрузке файла", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка загрузки файла. Попробуйте еще раз.");
            return "redirect:/account-settings";
        }
    }

    @PostMapping("/update-account")
    public String updateAccount(
            @RequestParam("username") String username,
            @RequestParam(value = "password", required = false) String password,
            RedirectAttributes redirectAttributes) {
        logger.info("Обработка запроса на обновление учетной записи");

        try {
            userService.updateUser(username, password);
            logger.info("Учетная запись пользователя {} успешно обновлена", username);
            redirectAttributes.addFlashAttribute("successMessage", "Настройки успешно обновлены.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            logger.warn("Ошибка при обновлении учетной записи: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/account-settings";
        } catch (Exception e) {
            logger.error("Ошибка при обновлении учетной записи", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении настроек. Попробуйте еще раз.");
            return "redirect:/account-settings";
        }
    }
}