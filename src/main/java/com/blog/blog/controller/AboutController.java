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

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public AboutController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/aboutUs")
    public String aboutUs(@RequestParam(value = "error", required = false) String error, Model model) {
        String currentUserName = accountService.getUserName();

        model.addAttribute("isAdmin", userService.isAdmin(currentUserName));
        model.addAttribute("currentUser", userService.getUserInfo(currentUserName));

        if (error != null) {
            model.addAttribute("errorMessage", "An error occurred during login.");
        }

        return "aboutUs";
    }
}
