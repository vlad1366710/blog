package com.blog.blog.service;


import com.blog.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {


    @Autowired
    private UserService userService;

    public User getUserInfo(){
        // Получаем информацию о текущем пользователе
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Получаем имя пользователя
        User currentUser = userService.findByUsername(currentUsername); // Получаем пользователя из базы данных

        return currentUser;
    }

    public boolean isAdmin(){
        boolean isAdmin;
        isAdmin = getUserInfo().isAdmin();
        return isAdmin;
    }
}
