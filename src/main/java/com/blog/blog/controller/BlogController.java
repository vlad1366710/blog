package com.blog.blog.controller;

import com.blog.blog.model.BlogPost;
import com.blog.blog.model.Comment;
import com.blog.blog.model.User;
import com.blog.blog.service.AccountService;
import com.blog.blog.service.BlogPostService;
import com.blog.blog.service.CommentService;
import com.blog.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BlogController {

    private final BlogPostService blogPostService;
    private final UserService userService;
    private final AccountService accountService;
    private final CommentService commentService;

    @Autowired
    public BlogController(BlogPostService blogPostService, UserService userService,
                          AccountService accountService, CommentService commentService) {
        this.blogPostService = blogPostService;
        this.userService = userService;
        this.accountService = accountService;
        this.commentService = commentService;
    }

    @GetMapping("/posts")
    public String getAllPosts(Model model) {
        model.addAttribute("posts", blogPostService.getAllPosts());
        return "posts"; // имя шаблона
    }

    @GetMapping("/posts/new")
    public String createPostForm(Model model) {
        model.addAttribute("post", new BlogPost());
        return "createPost";
    }

    @PostMapping("/posts")
    public String createPost(@ModelAttribute BlogPost post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUsername = authentication.getName();
            User currentUser = userService.findByUserLogin(currentUsername);
            if (currentUser  != null) {
                post.setAuthor(currentUser );
            }
        }

        blogPostService.createPost(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable Long id,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "5") int size,
                           Model model) {
        BlogPost post = blogPostService.getPostById(id);
        if (post == null) {
            return "redirect:/posts";
        }

        Page<Comment> commentsPage = commentService.findCommentsByPostId(id, PageRequest.of(page, size));
        model.addAttribute("post", post);
        model.addAttribute("comments", commentsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", commentsPage.getTotalPages());

        return "viewPost"; // имя шаблона
    }

    @GetMapping("/blog-center")
    public String blogCenter(@RequestParam(defaultValue = "") String query,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<BlogPost> postsPage = query.isEmpty() ?
                blogPostService.getAllPostss(pageable) :
                blogPostService.searchPosts(query, pageable);

        model.addAttribute("posts", postsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("query", query);
        model.addAttribute("isAdmin", userService.isAdmin(accountService.getUserName()));
        model.addAttribute("currentUser", userService.getUserInfo(accountService.getUserName()));

        return "blog-center";
    }

    @GetMapping("/posts/edit/{id}")
    public String editPost(@PathVariable("id") Long id, Model model) {
        BlogPost post = blogPostService.getPostById(id);
        if (post == null) {
            return "redirect:/posts";
        }
        model.addAttribute("post", post);
        return "editPost";
    }

    @PostMapping("/posts/edit")
    public String updatePost(@ModelAttribute("post") BlogPost post) {
        post.setAuthor(userService.getUserInfo(accountService.getUserName()));
        blogPostService.savePost(post);
        return "redirect:/posts";
    }

    @PostMapping("/posts/delet/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        blogPostService.deletePostById(id);
        return "redirect:/posts";
    }
}
