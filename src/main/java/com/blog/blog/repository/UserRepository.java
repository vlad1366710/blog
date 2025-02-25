package com.blog.blog.repository;

import com.blog.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String Login);

    User findByEmail(String Email);

    /**
     * Находит пользователя по токену подтверждения email.
     *
     * @param token Токен подтверждения email.
     * @return Пользователь с указанным токеном или null, если пользователь не найден.
     */
    User findByEmailConfirmationToken(String token);

    boolean existsByUsername(String username);
}



