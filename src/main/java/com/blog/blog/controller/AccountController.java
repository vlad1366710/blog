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

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/account-settings")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {


        model.addAttribute("currentUser",userService.getUserInfo(accountService.getUserName()));


        return "account-settings"; // Возвращает страницу логина
    }

    @PostMapping("/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file, RedirectAttributes redirectAttributes) {
        // Проверка, что файл не пустой
        String uploadDir = "C:\\Users\\user\\Desktop\\blog\\src\\main\\resources\\static\\image\\";
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Выберите файл для загрузки.");
            return "redirect:/account-settings"; // Перенаправление на страницу настроек аккаунта
        }

        try {
            String avatarPath = uploadDir + file.getOriginalFilename();
            // Создаем файл на сервере
            File uploadFile = new File(uploadDir + file.getOriginalFilename());
            file.transferTo(uploadFile); // Сохраняем файл
            User currentUser  = userService.getUserInfo(accountService.getUserName());
            // Вы можете добавить логику для сохранения информации о пользователе или аватаре в базе данных
            currentUser .setAvatarUrl("/image/" + file.getOriginalFilename()); // Устанавливаем новое значение поля
            userService.updateUser(currentUser );


            redirectAttributes.addFlashAttribute("message", "Аватар успешно загружен.");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки файла. Попробуйте еще раз.");
        }

        return "redirect:/account-settings"; // Перенаправление на страницу настроек аккаунта
    }


    @PostMapping("/update-account")
    public String updateAccount(
            @RequestParam("username") String username,
            @RequestParam(value = "password", required = false) String password,
            RedirectAttributes redirectAttributes) {
        try {
            // Обновляем информацию о пользователе
            userService.updateUser (username, password); // Используем новый метод обновления
            String name = accountService.getUserName();
            // Добавляем сообщение об успешном обновлении
            redirectAttributes.addFlashAttribute("successMessage", "Настройки успешно обновлены.");
            
            // Перенаправляем на страницу настроек аккаунта
            return "redirect:/login"; // Укажите правильный путь к странице настроек
        } catch (IllegalArgumentException e) {
            // Обработка ошибок (например, если пользователь не найден)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/account-settings"; // Перенаправляем обратно на страницу настроек
        } catch (Exception e) {
            // Обработка других исключений
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении настроек. Попробуйте еще раз.");
            return "redirect:/account-settings"; // Перенаправляем обратно на страницу настроек
        }
    }

}



