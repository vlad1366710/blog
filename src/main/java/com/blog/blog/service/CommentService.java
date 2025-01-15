package com.blog.blog.service;

import com.blog.blog.model.BlogPost;
import com.blog.blog.model.Comment;
import com.blog.blog.model.User;
import com.blog.blog.repository.CommentRepository;
import com.blog.blog.repository.BlogPostRepository; // Импортируйте репозиторий для постов
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogPostRepository blogPostRepository; // Для получения поста по ID

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        // Логика для получения комментариев по ID поста
        return commentRepository.findByBlogPostId(postId);
    }

    public Comment addCommentToPost(Long postId, Comment comment) {
        // Убедитесь, что пост с таким ID существует
        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        comment.setBlogPost(blogPost); // Устанавливаем пост для комментария

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUserLogin = authentication.getName(); // Получаем имя пользователя

            // Получаем пользователя из базы данных по имени
            User currentUser = userService.findByUserLogin(currentUserLogin); // Убедитесь, что метод возвращает CustomUser
            if (currentUser != null) {
                comment.setUser(currentUser); // Устанавливаем объект пользователя как автора поста
            }
        }
        return commentRepository.save(comment);
    }

    public Page<Comment> findCommentsByPostId(Long postId, Pageable pageable) {
        return (Page<Comment>) commentRepository.findByBlogPostId(postId,  pageable);
    }


    // Другие методы для работы с комментариями
}
