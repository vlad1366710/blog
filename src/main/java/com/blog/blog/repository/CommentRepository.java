package com.blog.blog.repository;

import com.blog.blog.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBlogPostId(Long postId); // Метод для получения комментариев по ID поста

    // Измените метод на правильное имя и тип возвращаемого значения
    Page<Comment> findByBlogPostId(Long postId, Pageable pageable);
}
