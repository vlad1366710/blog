package com.blog.blog.controller;

import com.blog.blog.model.User;
import com.blog.blog.service.AccountService;
import com.blog.blog.service.UserService;
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

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public AccountController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/account-settings")
    public String accountSettings(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("currentUser", userService.getUserInfo(accountService.getUserName()));
        return "account-settings";
    }

    @PostMapping("/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file, RedirectAttributes redirectAttributes) {
        String uploadDir = "C:\\Users\\user\\Desktop\\blog\\src\\main\\resources\\static\\image\\";


        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Выберите файл для загрузки.");
            return "redirect:/account-settings";
        }

        try {
            String avatarPath = uploadDir + file.getOriginalFilename();
            File uploadFile = new File(avatarPath);
            file.transferTo(uploadFile);

            User currentUser  = userService.getUserInfo(accountService.getUserName());
            currentUser .setAvatarUrl("/image/" + file.getOriginalFilename());
            userService.updateUser (currentUser );

            redirectAttributes.addFlashAttribute("message", "Аватар успешно загружен.");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки файла. Попробуйте еще раз.");
        }

        return "redirect:/account-settings";
    }

    @PostMapping("/update-account")
    public String updateAccount(
            @RequestParam("username") String username,
            @RequestParam(value = "password", required = false) String password,
            RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser (username, password);
            redirectAttributes.addFlashAttribute("successMessage", "Настройки успешно обновлены.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/account-settings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении настроек. Попробуйте еще раз.");
            return "redirect:/account-settings";
        }
    }
}
