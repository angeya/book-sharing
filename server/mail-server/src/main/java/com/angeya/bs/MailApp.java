package com.angeya.bs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MailApp {
    public static void main(String[] args) {
        SpringApplication.run(MailApp.class, args);
        System.out.println("I write this with vim plugin");
    }
}
