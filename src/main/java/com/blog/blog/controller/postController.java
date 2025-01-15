package com.blog.blog.controller;

import com.blog.blog.model.BlogPost;
import com.blog.blog.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class postController {

    @Autowired
    private BlogPostService blogPostService;


    @GetMapping("/user-posts/{userId}")
    public String getUserPosts(@PathVariable Long userId, Model model, Principal principal) {
        // Получаем посты пользователя по его ID
        List<BlogPost> userPosts = blogPostService.getPostsByUserId(userId);

        // Добавляем посты в модель
        model.addAttribute("posts", userPosts);

        // Возвращаем имя шаблона для отображения постов
        return "posts"; // Имя шаблона, который будет отображать посты пользователя
    }

}
