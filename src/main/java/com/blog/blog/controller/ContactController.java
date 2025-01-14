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

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @RequestMapping("/contacts")
    public String showContactForm(Model model) {
        return "contacts"; // Убедитесь, что у вас есть шаблон contacts.html
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("message") String message,
                              RedirectAttributes redirectAttributes) {
        try {
            emailService.sendEmail(name, email, message);
            redirectAttributes.addFlashAttribute("successMessage", "Сообщение успешно отправлено.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при отправке сообщения. Попробуйте еще раз.");
        }
        return "redirect:/contacts"; // Перенаправление обратно на страницу контактов
    }
}