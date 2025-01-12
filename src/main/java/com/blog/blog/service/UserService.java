package com.blog.blog.service;

import com.blog.blog.model.User;
import com.blog.blog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
