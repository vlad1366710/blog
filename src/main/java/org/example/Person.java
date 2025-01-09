package org.example;

import org.example.Annotations.ToString;

@ToString
public class Person {
    @ToString(value = "NO")
    private String password;

    @ToString
    private String name;

    public Person(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
