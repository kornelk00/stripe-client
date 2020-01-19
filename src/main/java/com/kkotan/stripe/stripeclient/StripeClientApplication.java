package com.kkotan.stripe.stripeclient;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.kkotan.stripe.stripeclient.utils.ConsoleReader;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

@SpringBootApplication
public class StripeClientApplication {
	private static final Logger logger = LogManager.getLogger(StripeClientApplication.class);
	private static ConsoleReader consoleReader;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(StripeClientApplication.class, args);
		try {
			GoUrbanStripeClient stripeClient = context.getBean(GoUrbanStripeClient.class);
			ConsoleReader consoleReader = context.getBean(ConsoleReader.class);

			System.out.println("Welcome!");

			String emailAddress = consoleReader.readEmailAddress();

			Customer customer = stripeClient.registerCustomerWithCard(emailAddress);

			Optional<Long> amount = consoleReader.readAmountFromConsole();

			Charge charge = stripeClient.chargeCustomerInEuro(amount, customer);

			Optional<String> captureOrRefundLetter = consoleReader.readCaptureOrRefundLetter();

			stripeClient.captureOrRefundCharge(charge, captureOrRefundLetter);

			consoleReader.close();
		} catch (StripeException e) {
			System.out.println("Exception happened while communcating with Stripe: " + e.getMessage());
			logger.error(e.getMessage());
			consoleReader.close();
		} catch (Exception e) {
			System.out.println("Exception happened while processing");
			logger.error(e.getMessage());
		}
	}
}