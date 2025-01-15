package com.blog.blog.controller;

import com.blog.blog.model.BlogPost;
import com.blog.blog.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;


@Controller
public class PostController {

    private final BlogPostService blogPostService;

    @Autowired
    public PostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }


    @GetMapping("/user-posts/{userId}")
    public String getUserPosts(@PathVariable Long userId, Model model, Principal principal) {

        List<BlogPost> userPosts = blogPostService.getPostsByUserId(userId);

        model.addAttribute("posts", userPosts);

        return "posts";
    }
}
