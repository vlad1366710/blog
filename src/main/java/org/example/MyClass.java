package org.example;

import org.example.Annotations.Default;

@Default(value = String.class)
public class MyClass {
    @Default(value = Integer.class)
    private int myField;

    public int getMyField() {
        return myField;
    }

    public void setMyField(int myField) {
        this.myField = myField;
    }
}
