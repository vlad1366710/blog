package com.blog.blog.service;

import com.blog.blog.model.BlogPost;
import com.blog.blog.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;

    public List<BlogPost> getAllPosts() {
        return blogPostRepository.findAll();
    }

    public void createPost(BlogPost post) {
        blogPostRepository.save(post);
    }

    public BlogPost getPostById(Long id) {
        Optional<BlogPost> post = blogPostRepository.findById(id);
        return post.orElse(null); // Возвращаем пост или null, если не найден
    }
}
