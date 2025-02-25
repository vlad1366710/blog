package com.blog.blog.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail() {
        String host = "smtp.gmail.com"; // SMTP сервер
        final String user = "vladmuz369@gmail.com"; // Ваш email
        final String password = "password"; // Ваш пароль или пароль приложения

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true"); // Исправлено
        props.put("mail.smtp.port", "587"); // Порт для TLS
        props.put("mail.smtp.starttls.enable", "true"); // Включить TLS

        String to = "vlad.psu@mail.ru"; // Получатель

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Test Subject");
            message.setText("This is a test email!");

            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
