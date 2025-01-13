package com.blog.blog.repository;

import com.blog.blog.model.BlogPost;
import com.blog.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByAuthor(User author);

    Page<BlogPost> findAll(Pageable pageable);
}