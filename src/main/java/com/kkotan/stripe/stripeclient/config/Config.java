package com.kkotan.stripe.stripeclient.config;

import java.util.Scanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public Scanner getScanner() {
        return new Scanner(System.in);
    }
}