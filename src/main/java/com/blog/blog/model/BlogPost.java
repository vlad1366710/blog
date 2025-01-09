package com.blog.blog.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;

@Entity
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne // Указываем, что это связь "многие к одному"
    @JoinColumn(name = "author_id") // Указываем имя колонки в базе данных
    private User  author; // Измените тип на объект CustomUser

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

    public User  getAuthor() { // Измените тип возвращаемого значения на CustomUser
        return author;
    }

    public void setAuthor(User  author) { // Измените тип аргумента на CustomUser
        this.author = author;
    }
}
