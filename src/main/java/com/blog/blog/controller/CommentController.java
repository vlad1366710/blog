package com.blog.blog.controller;

import com.blog.blog.model.Comment;
import com.blog.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String getAllComments(@PathVariable Long postId, Model model) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        model.addAttribute("comments", comments);
        return "comments"; // Имя вашего HTML-шаблона для отображения комментариев
    }

    @PostMapping
    public String addComment(@PathVariable Long postId, @RequestParam String content) {
        Comment comment = new Comment();
        comment.setContent(content);

        // Сохраняем комментарий
        commentService.addCommentToPost(postId, comment);

        // Перенаправляем на страницу поста после добавления комментария
        return "redirect:/posts/" + postId; // Замените на свой маршрут для отображения поста
    }

    // Другие методы для работы с комментариями
}
