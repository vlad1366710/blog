package com.blog.blog.service;


import com.blog.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {




    public String getUserName(){
        // Получаем информацию о текущем пользователе
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Получаем имя пользователя

        return currentUsername;
    }


}

