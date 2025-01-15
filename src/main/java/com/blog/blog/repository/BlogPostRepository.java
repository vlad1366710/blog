package com.blog.blog.repository;

import com.blog.blog.model.BlogPost;
import com.blog.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findByAuthor(User author);

    Page<BlogPost> findAll(Pageable pageable);

    List<BlogPost> findByAuthorId(Long authorId);

    Page<BlogPost> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Modifying
    @Query("DELETE FROM BlogPost b WHERE b.author.id = :authorId")
    void deleteByAuthorId(@Param("authorId") Long authorId);
}
