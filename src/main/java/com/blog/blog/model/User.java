package com.blog.blog.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Id;

import java.time.LocalDateTime; // Импортируйте LocalDateTime
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private boolean isAdmin; // Поле для проверки прав администратора
    private boolean isActive; // Поле для проверки активности пользователя
    private String avatarUrl; // Поле для URL аватара
    private String email; // Поле для хранения email
    private String role; // Новое поле для хранения роли пользователя

    private LocalDateTime lastLogin; // Добавлено поле для хранения времени последнего входа

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() { // Геттер для email
        return email;
    }

    public void setEmail(String email) { // Сеттер для email
        this.email = email;
    }

    public String getRole() { // Геттер для role
        return role;
    }

    public void setRole(String role) { // Сеттер для role
        this.role = role;
    }

    public LocalDateTime getLastLogin() { // Геттер для lastLogin
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) { // Сеттер для lastLogin
        this.lastLogin = lastLogin;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
