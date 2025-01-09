package org.example;

import org.example.Annotations.Validate;

@Validate(value = {String.class, Integer.class}) // Аннотируем класс с указанием классов для валидации
public class User {
    private String username;
    private int age;

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }
}


