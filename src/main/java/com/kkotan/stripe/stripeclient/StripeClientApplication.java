package com.kkotan.stripe.stripeclient;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Source;

@SpringBootApplication
public class StripeClientApplication {
	private static final Logger logger = LogManager.getLogger(StripeClientApplication.class);

	private static final String CURRENCY_EUR = "eur";
	private static final String SOURCE_TYPE = "card";
	private static final String TOKEN = "tok_at";
	private static final String API_KEY = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";

	public static void main(String[] args) {
		SpringApplication.run(StripeClientApplication.class, args);
		logger.info("Info log");
		StripeConnector.setApiKey(API_KEY);
		
		Scanner scanner = new Scanner(System.in);
		
		try {
			
			
			System.out.println("Welcome! Please enter email address for Stripe registration");
			String emailAddress = scanner.nextLine();
			System.out.println("Registering customer with email address: " + emailAddress);
			
			Customer customer = StripeConnector.createCustomer(emailAddress);
			Source source = StripeConnector.createSourceForCustomer(TOKEN, SOURCE_TYPE, customer);
			
			System.out.println("Customer succesfully registered");
			
			System.out.println("How many euros would you like to charge?");
			Long amount = scanner.nextLong();
			System.out.println("Creating charge of " + amount);
			Charge charge = StripeConnector.createChargeForSource(amount, CURRENCY_EUR, source);
			
			System.out.println("Charge created. Would you like to capture(C) or refund(R)?");
			String letter = scanner.next();
			
			if(letter.equals("C")) {
				System.out.println("Capturing charge..");
				StripeConnector.captureCharge(charge);
				System.out.println("Charge succesfully captured");
			} else if (letter.equals("R")) {
				System.out.println("Refunding charge..");
				StripeConnector.refundCharge(charge);
				System.out.println("Charge succesfully refunded");
			} else {
				System.out.println("Wrong input");
			}
			
			scanner.close();
			
			
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			scanner.close();
		}
		
	}

}
