package com.blog.blog.controller;

import com.blog.blog.service.AccountService;
import com.blog.blog.service.EmailService;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    private final EmailService emailService;
    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public ContactController(EmailService emailService, UserService userService, AccountService accountService) {
        this.emailService = emailService;
        this.userService = userService;
        this.accountService = accountService;
    }


    @RequestMapping("/contacts")
    public String showContactForm(Model model) {
        return "contacts";
    }


    @PostMapping("/send-message")
    public String sendMessage(@RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("message") String message,
                              RedirectAttributes redirectAttributes) {
        try {
            emailService.sendEmail();
            redirectAttributes.addFlashAttribute("successMessage", "Сообщение успешно отправлено.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при отправке сообщения. Попробуйте еще раз.");
        }
        return "redirect:/contacts";
    }
}
