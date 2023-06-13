package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoLambdaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoLambdaApplication.class, args);

        // C1: Sử dụng class Impliment
        Hello hello = new User();
        hello.sayHello();

        // C2: Sử dụng Anonymous class
        Hello hello1 = new Hello() {
            @Override
            public void sayHello() {
                System.out.println("Hello 1❤️");
            }
        };
        hello1.sayHello();

        // C3 : Sử dụng Lambda
        Hello hello2 = () -> System.out.println("Hello 2😍");
        hello2.sayHello();

        // Tham khảo các build-in functional interface có sẵn : https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html
    }
}
