package com.blog.blog.service;

import com.blog.blog.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class AccountService {

    // Логгер для класса AccountService
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    // Директория для загрузки файлов
    private static final String UPLOAD_DIR = "C:\\Users\\user\\Desktop\\blog\\src\\main\\resources\\static\\image\\";

    private final UserService userService;

    // Удаляем циклическую зависимость (AccountService не должен зависеть от самого себя)
    public AccountService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получает имя текущего аутентифицированного пользователя.
     *
     * @return Имя пользователя.
     */
    public String getUserName() {
        logger.debug("Получение имени текущего пользователя");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.debug("Текущий пользователь: {}", username);
        return username;
    }

    /**
     * Загружает аватар пользователя.
     *
     * @param file Файл аватара.
     * @throws IllegalArgumentException Если файл пустой.
     * @throws IOException Если произошла ошибка при загрузке файла.
     */
    public void uploadAvatar(MultipartFile file) throws IOException {
        logger.info("Начало загрузки аватара");

        if (file.isEmpty()) {
            logger.warn("Файл для загрузки не выбран");
            throw new IllegalArgumentException("Файл для загрузки не выбран");
        }

        try {
            // Сохраняем файл на сервере
            String avatarPath = UPLOAD_DIR + file.getOriginalFilename();
            File uploadFile = new File(avatarPath);
            file.transferTo(uploadFile);

            // Обновляем аватар пользователя в базе данных
            User currentUser = userService.getUserInfo(getUserName());
            currentUser.setAvatarUrl("/image/" + file.getOriginalFilename());
            userService.updateUser(currentUser);

            logger.info("Аватар успешно загружен для пользователя: {}", currentUser.getUsername());
        } catch (IOException e) {
            logger.error("Ошибка при загрузке файла", e);
            throw new IOException("Ошибка при загрузке файла", e);
        }
    }
}