package com.blog.blog.repository;

import com.blog.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Поиск пользователя по username
    Optional<User> findByUsername(String username);

    // Поиск пользователя по userLogin
    Optional<User> findByUserLogin(String userLogin);

    // Поиск пользователя по email
    User findByEmail(String email);

    // Поиск пользователя по токену подтверждения email
    User findByEmailConfirmationToken(String token);

    // Проверка существования пользователя по username
    boolean existsByUsername(String username);

    // Проверка существования пользователя по userLogin
    boolean existsByUserLogin(String userLogin);

    // Проверка существования пользователя по email
    boolean existsByEmail(String email);
}


