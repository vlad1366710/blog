package com.blog.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String name, String email, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("vladmuz369@gmail.com"); // Замените на свой рабочий email
        mailMessage.setSubject("Новое сообщение от " + name);
        mailMessage.setText("Сообщение от: " + name + "\nEmail: " + email + "\n\nСообщение:\n" + message);

        try {
            mailSender.send(mailMessage);
            logger.info("Письмо успешно отправлено на {}", mailMessage.getTo()[0]);
        } catch (Exception e) {
            logger.error("Ошибка при отправке письма: {}", e.getMessage());
        }
    }
}
