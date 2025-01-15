package com.blog.blog.service;

import com.blog.blog.model.BlogPost;
import com.blog.blog.model.Comment;
import com.blog.blog.model.User;
import com.blog.blog.repository.CommentRepository;
import com.blog.blog.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogPostRepository blogPostRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByBlogPostId(postId);
    }

    public Comment addCommentToPost(Long postId, Comment comment) {
        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        comment.setBlogPost(blogPost);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUserLogin = authentication.getName();


            User currentUser  = userService.findByUserLogin(currentUserLogin);
            if (currentUser  != null) {
                comment.setUser (currentUser );
            }
        }
        return commentRepository.save(comment);
    }

    public Page<Comment> findCommentsByPostId(Long postId, Pageable pageable) {
        return commentRepository.findByBlogPostId(postId, pageable);
    }
}
