package com.blog.blog.controller;

import com.blog.blog.service.AccountService;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ContactsController {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public ContactsController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }


    @GetMapping("/contacts")
    public String showContacts(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("currentUser", userService.getUserInfo(accountService.getUserName()));
        model.addAttribute("isAdmin", userService.isAdmin(accountService.getUserName()));


        if (error != null) {
            model.addAttribute("errorMessage", "Ошибка при входе. Пожалуйста, проверьте свои учетные данные.");
        }

        return "contacts";
    }
}
