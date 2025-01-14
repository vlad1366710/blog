package com.blog.blog.controller;

import com.blog.blog.model.BlogPost;
import com.blog.blog.model.User;
import com.blog.blog.service.AccountService;
import com.blog.blog.service.BlogPostService;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@Controller
public class BlogController {
    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

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
            User currentUser = userService.findByUserLogin(currentUsername); // Убедитесь, что метод возвращает CustomUser
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
    public String blogCenter(@RequestParam(defaultValue = "") String query,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {
        // Создаем объект Pageable для пагинации
        Pageable pageable = PageRequest.of(page, 10); // 10 постов на странице

        Page<BlogPost> postsPage;

        if (query.isEmpty()) {
            // Если запрос пустой, получаем все посты
            postsPage = blogPostService.getAllPostss(pageable);
        } else {
            // Если есть запрос, выполняем поиск
            postsPage = blogPostService.searchPosts(query, pageable);
        }

        model.addAttribute("posts", postsPage.getContent()); // Получаем содержимое текущей страницы
        model.addAttribute("currentPage", page); // Текущая страница
        model.addAttribute("totalPages", postsPage.getTotalPages()); // Общее количество страниц
        model.addAttribute("query", query); // Добавляем поисковый запрос в модель
        model.addAttribute("isAdmin", userService.isAdmin(accountService.getUserName())); // Добавляем переменную isAdmin в модель
        model.addAttribute("currentUser", userService.getUserInfo(accountService.getUserName()));

        return "blog-center"; // Имя вашего представления
    }


// /posts/edit/1
    // http://localhost:8080/posts/1
}
