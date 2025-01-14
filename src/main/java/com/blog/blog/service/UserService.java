package com.blog.blog.service;

import com.blog.blog.model.User;
import com.blog.blog.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    public void registerUser (User user) throws IllegalArgumentException {
        validatePassword(user.getPassword()); // Проверка пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 8 символов.");
        }
        // Добавьте дополнительные условия по мере необходимости
    }

    public User findByUserLogin(String Login) {
        return userRepository.findByUsername(Login);
    }
    public static final int USERS_PER_PAGE = 10;

    public List<User> getUsers(int page) {
        int offset = (page - 1) * USERS_PER_PAGE;
        return userRepository.findAll(PageRequest.of(page - 1, USERS_PER_PAGE)).getContent(); // Используйте пагинацию
    }

    public int getTotalUsers() {
        return (int) userRepository.count();
    }

    public void updateUser (String newUsername, String newPassword) {
        User user = getUserInfo(accountService.getUserName());
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден.");
        }

        if (newUsername != null) {
            user.setUsername(newUsername);
        }

        // Если новый пароль предоставлен, обновляем его
        if (newPassword != null && !newPassword.isEmpty()) {
            validatePassword(newPassword); // Проверка нового пароля
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        // Обновляем информацию о пользователе в базе данных
        userRepository.save(user);
    }


    public void updateUser (User user) {
        userRepository.save(user); // Сохраняем изменения в базе данных
    }

    public User getUserInfo(String currentUserLogin) {
        User currentUser = findByUserLogin(currentUserLogin);
        return currentUser;
    }

    public boolean isAdmin(String currentUsername) {
        User currentUser  = getUserInfo(currentUsername); // Получаем информацию о текущем пользователе
        return currentUser  != null && currentUser .isAdmin(); // Проверяем, что пользователь не null и является администратором
    }

}
