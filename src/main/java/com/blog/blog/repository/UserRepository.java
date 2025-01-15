package com.blog.blog.repository;

import com.blog.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String Login);
}
