package com.blog.blog.repository;

import com.blog.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBlogPostId(Long postId); // Метод для получения комментариев по ID поста
}
