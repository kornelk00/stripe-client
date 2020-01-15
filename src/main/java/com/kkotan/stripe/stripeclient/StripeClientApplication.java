package com.kkotan.stripe.stripeclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StripeClientApplication {
	private static final Logger logger = LogManager.getLogger(StripeClientApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(StripeClientApplication.class, args);
		logger.info("Info log");
	}

}
