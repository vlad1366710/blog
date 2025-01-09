package org.example;

import org.example.Annotations.Invoke;

public class Example {

    @Invoke
    public void myAnnotatedMethod() {
        System.out.println("Этот метод аннотирован @Invoke.");
    }
}

