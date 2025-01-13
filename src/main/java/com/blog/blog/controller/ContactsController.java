package com.blog.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactsController {

    @GetMapping("/contacts")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        return "contacts"; // Возвращает страницу логина
    }
}
