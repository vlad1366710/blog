package com.blog.blog.controller;

import com.blog.blog.model.Comment;
import com.blog.blog.service.CommentService;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments") // Измените путь на /posts/{postId}/comments
public class CommentController {

    @Autowired
    private CommentService commentService;



    @GetMapping
    public List<Comment> getAllComments(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId); // Метод для получения комментариев по ID поста
    }

    @PostMapping
    public Comment addComment(@PathVariable Long postId, @RequestParam String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        return commentService.addCommentToPost(postId, comment);
    }


    // Другие методы для работы с комментариями
}
