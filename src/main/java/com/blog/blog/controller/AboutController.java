package com.blog.blog.controller;

import com.blog.blog.service.AccountService;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AboutController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;


    @GetMapping("/aboutUs")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("isAdmin", userService.isAdmin(accountService.getUserName())); // Добавляем переменную isAdmin в модель
        model.addAttribute("currentUser", userService.getUserInfo(accountService.getUserName()));
        return "aboutUs"; // Возвращает страницу логина
    }
}
