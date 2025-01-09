package org.example;

import org.example.Annotations.ToString;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Entity {
    @Override
    public String toString() {
        List<String> fieldStrings = new ArrayList<>();
        Class<?> currentClass = this.getClass();
        boolean atLeastOneFieldIncluded = false;

        while (currentClass != null) {
            Field[] fields = currentClass.getDeclaredFields();
            boolean allFieldsExcluded = true;

            for (Field field : fields) {
                field.setAccessible(true);

                ToString toStringAnnotation = field.getAnnotation(ToString.class);

                if (toStringAnnotation == null || "YES".equals(toStringAnnotation.value())) {
                    try {
                        fieldStrings.add(field.getName() + "=" + field.get(this));
                        atLeastOneFieldIncluded = true;
                        allFieldsExcluded = false;
                    } catch (IllegalAccessException e) {
                        fieldStrings.add(field.getName() + "=ERROR");
                    }
                }

            }


            if (allFieldsExcluded) {
                if (currentClass.getSuperclass() != null) {
                    currentClass = currentClass.getSuperclass();
                    continue;
                } else {
                    break;
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        if (!atLeastOneFieldIncluded) {
            return getClass().getSimpleName() + "{}";
        }

        return getClass().getSimpleName() + "{" + String.join(",", fieldStrings) + "}";
    }
}
