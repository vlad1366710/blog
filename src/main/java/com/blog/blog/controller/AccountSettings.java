package com.blog.blog.controller;

import com.blog.blog.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountSettings {
    @Autowired
    private AccountService accountService;

    @GetMapping("/account-settings")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {


        model.addAttribute("currentUser", accountService.getUserInfo());


        return "account-settings"; // Возвращает страницу логина
    }
}
