package com.blog.blog.service;

import com.blog.blog.model.BlogPost;
import com.blog.blog.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;


    public List<BlogPost> getAllPosts() {
        return blogPostRepository.findAll();
    }


    public Page<BlogPost> getAllPostss(Pageable pageable) {
        return blogPostRepository.findAll(pageable);
    }

    public void createPost(BlogPost post) {
        blogPostRepository.save(post);
    }

    public BlogPost getPostById(Long id) {
        Optional<BlogPost> post = blogPostRepository.findById(id);
        return post.orElse(null); // Возвращаем пост или null, если не найден
    }

    public Page<BlogPost> searchPosts(String query, Pageable pageable) {
        return blogPostRepository.findByTitleContainingIgnoreCase(query, pageable);
    }

    // Сохранение или обновление поста
    public BlogPost savePost(BlogPost post) {
        return blogPostRepository.save(post); // Возвращаем сохраненный пост
    }

    public void deletePostById(Long id) {
        blogPostRepository.deleteById(id); // Удаление поста по ID
    }
}
