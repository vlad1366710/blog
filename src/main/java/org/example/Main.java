package org.example;

import org.example.Annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Выберите задание:");
            System.out.println("1(1). К строке");
            System.out.println("2(2.1). @Invoke");
            System.out.println("3(2.2). @Default");
            System.out.println("4(2.3). @ToString");
            System.out.println("5(2.4). @Validate");
            System.out.println("6(2.5). @Two");
            System.out.println("7(2.6). @Cache");
            System.out.println("8(3.1). К строке2.");
            System.out.println("0. Выход");
            System.out.print("Ваш выбор: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        printEmptyLines();
                        line();
                        printEmptyLines();
                        break;
                    case 2:
                        printEmptyLines();
                        Invoke();
                        printEmptyLines();
                        break;
                    case 3:
                        printEmptyLines();
                        Default();
                        printEmptyLines();
                        break;
                    case 4:
                        printEmptyLines();
                        ToString();
                        printEmptyLines();
                        break;
                    case 5:
                        printEmptyLines();
                        Validate();
                        printEmptyLines();
                        break;
                    case 6:
                        printEmptyLines();
                        Two();
                        printEmptyLines();
                        break;
                    case 7:
                        printEmptyLines();
                        Cache();
                        printEmptyLines();
                        break;
                    case 8:
                        printEmptyLines();
                        line2();
                        printEmptyLines();
                        break;
                    case 0:
                        System.out.println("Выход из программы.");
                        return;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка ввода. Пожалуйста, введите число от 0 до 8.");
                scanner.nextLine();
            }
        }
    }

        private static void printEmptyLines() {
            System.out.println();
            System.out.println();
            System.out.println();
        }

        public static void line() {
            B n = new B();
            System.out.println(n);
        }

        public static void Invoke() {
            Example example = new Example();
            example.myAnnotatedMethod(); // Вызов метода

            java.lang.reflect.Method[] methods = Example.class.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Invoke.class)) {
                    System.out.println("Метод " + method.getName() + " аннотирован @Invoke.");
                }
            }

        }
    public static void Default() {
        Class<MyClass> myClass = MyClass.class;
        if (myClass.isAnnotationPresent(Default.class)) {
            Default defaultAnnotation = myClass.getAnnotation(Default.class);
            System.out.println("Класс " + myClass.getName() + " аннотирован @Default с value: " + defaultAnnotation.value().getName());
        }

        Field[] fields = myClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Default.class)) {
                Default defaultAnnotation = field.getAnnotation(Default.class);
                System.out.println("Поле " + field.getName() + " аннотировано @Default с value: " + defaultAnnotation.value().getName());
            }
        }
    }

    public static void ToString() {
        Class<Person> personClass = Person.class;
        if (personClass.isAnnotationPresent(ToString.class)) {
            ToString toStringAnnotation = personClass.getAnnotation(ToString.class);
            System.out.println("Класс " + personClass.getName() + " аннотирован @ToString с value: " + toStringAnnotation.value());
        }

        Field[] fields2 = personClass.getDeclaredFields();
        for (Field field : fields2) {
            if (field.isAnnotationPresent(ToString.class)) {
                ToString toStringAnnotation = field.getAnnotation(ToString.class);
                System.out.println("Поле " + field.getName() + " аннотировано @ToString с value: " + toStringAnnotation.value());
            }
        }
    }

    public static void Validate() {
        Class<User> userClass = User.class;

        if (userClass.isAnnotationPresent(Validate.class)) {
            Validate validateAnnotation = userClass.getAnnotation(Validate.class);
            System.out.println("Класс " + userClass.getName() + " аннотирован @Validate с value: ");
            for (Class<?> clazz : validateAnnotation.value()) {
                System.out.println(" - " + clazz.getName());
            }
        }

    }
    public static void Two() {
        Class<ExampleClass> exampleClass = ExampleClass.class;

        if (exampleClass.isAnnotationPresent(Two.class)) {
            Two twoAnnotation = exampleClass.getAnnotation(Two.class);
            System.out.println("Класс " + exampleClass.getName() + " аннотирован @Two с first: " +
                    twoAnnotation.first() + ", second: " + twoAnnotation.second());
        }
    }

    public static void Cache() {
        Class<CachedData> cachedDataClass = CachedData.class;

        if (cachedDataClass.isAnnotationPresent(Cache.class)) {
            Cache cacheAnnotation = cachedDataClass.getAnnotation(Cache.class);
            System.out.println("Класс " + cachedDataClass.getName() + " аннотирован @Cache с value: ");
            for (String value : cacheAnnotation.value()) {
                System.out.println(" - " + value);
            }
        }
    }
    public static void line2() {
        A a = new A();
        System.out.println(a);
    }
 }










