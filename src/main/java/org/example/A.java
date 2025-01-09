package org.example;

import org.example.Annotations.ToString;

class A extends Entity {
    String s = "hello";
    @ToString(value = "NO")
    int x= 42;
}