package com.kkotan.stripe.stripeclient.config;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kkotan.stripe.stripeclient.connector.StripeConnector;

@Configuration
public class Config {
	
	@Value("${stripe.api.key}")
	private String apiKey;
	
    @Bean
    public Scanner getScanner() {
        return new Scanner(System.in);
    }
    
    
    @Bean
    public StripeConnector getStripeConnector() {
        return new StripeConnector(apiKey);
    }
}