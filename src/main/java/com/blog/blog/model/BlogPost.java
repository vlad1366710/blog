package com.blog.blog.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne // Указываем, что это связь "многие к одному"
    @JoinColumn(name = "author_id") // Указываем имя колонки в базе данных
    private User author; // Измените тип на объект CustomUser , если это необходимо

    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Исправлено на blogPost
    private Set<Comment> comments;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() { // Измените тип возвращаемого значения на CustomUser , если это необходимо
        return author;
    }

    public void setAuthor(User author) { // Измените тип аргумента на CustomUser , если это необходимо
        this.author = author;
    }

    public Set<Comment> getComments() { // Добавляем геттер для comments
        return comments;
    }

    public void setComments(Set<Comment> comments) { // Добавляем сеттер для comments
        this.comments = comments;
    }
}
