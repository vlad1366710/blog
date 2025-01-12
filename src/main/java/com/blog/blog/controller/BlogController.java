package com.blog.blog.controller;

import com.blog.blog.model.BlogPost;
import com.blog.blog.model.User;
import com.blog.blog.service.BlogPostService;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BlogController {
    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private UserService userService;

    @GetMapping("/posts")
    public String getAllPosts(Model model) {
        model.addAttribute("posts", blogPostService.getAllPosts());
        return "posts"; // имя шаблона
    }

    @GetMapping("/posts/new")
    public String createPostForm(Model model) {
        model.addAttribute("post", new BlogPost());
        return "createPost"; // имя шаблона
    }

    @PostMapping("/posts")
    public String createPost(@ModelAttribute BlogPost post, @RequestParam("title") String title,
                             @RequestParam("content") String content) {
        post.setTitle(title);
        post.setContent(content);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUsername = authentication.getName(); // Получаем имя пользователя

            // Получаем пользователя из базы данных по имени
            User currentUser = userService.findByUsername(currentUsername); // Убедитесь, что метод возвращает CustomUser
            if (currentUser != null) {
                post.setAuthor(currentUser); // Устанавливаем объект пользователя как автора поста
            }
        }

        blogPostService.createPost(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        // Получаем пост по его идентификатору
        BlogPost post = blogPostService.getPostById(id);

        // Если пост не найден, перенаправляем на страницу со списком постов
        if (post == null) {
            return "redirect:/posts"; // или можно вернуть "404" страницу
        }

        // Добавляем пост в модель, чтобы он был доступен в шаблоне
        model.addAttribute("post", post);

        // Возвращаем имя шаблона для отображения поста
        return "viewPost"; // Убедитесь, что у вас есть шаблон viewPost.html
    }

    @GetMapping("/blog-center")
    public String blogCenter(Model model) {
        model.addAttribute("posts", blogPostService.getAllPosts());

        // Получаем информацию о текущем пользователе
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;

        if (authentication != null && authentication.isAuthenticated()) {
            String currentUsername = authentication.getName(); // Получаем имя пользователя
            User currentUser  = userService.findByUsername(currentUsername); // Получаем пользователя из базы данных

            if (currentUser  != null) {
                isAdmin = currentUser .isAdmin(); // Проверяем, является ли пользователь администратором
            }
        }

        model.addAttribute("isAdmin", isAdmin); // Добавляем переменную isAdmin в модель
        return "blog-center"; // Имя вашего представления
    }

// /posts/edit/1
    // http://localhost:8080/posts/1
}
