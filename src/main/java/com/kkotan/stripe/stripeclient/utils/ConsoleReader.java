package com.kkotan.stripe.stripeclient.utils;

import java.util.Optional;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsoleReader {
	private static final Logger logger = LogManager.getLogger(ConsoleReader.class);
	
	@Autowired
	private Scanner scanner;

	public Optional<String> readCaptureOrRefundLetter() {
		System.out.println("Would you like to capture(C) or refund(R)?");
		Optional<String> captureOrRefundLetter = Optional.empty();
		while (!captureOrRefundLetter.isPresent()) {
			try {
				if (scanner.hasNext()) {
					captureOrRefundLetter = Optional.ofNullable(scanner.next());
					if (!captureOrRefundLetter.get().toLowerCase().equals("c")
							& !captureOrRefundLetter.get().toLowerCase().equals("r")) {
						captureOrRefundLetter = Optional.empty();
						throw new IllegalArgumentException("The input didn't match C or R");
					}
				}
			} catch (Exception e) {
				System.out.println("Wrong input, Please make sure you enter C or R");
				logger.error(e.getMessage());
				scanner.nextLine();
			}
		}
		return captureOrRefundLetter;
	}

	public Optional<Long> readAmountFromConsole() {
		System.out.println("How many euros would you like to charge?");
		Optional<Long> amount = Optional.empty();
		while (!amount.isPresent()) {
			try {
				if (scanner.hasNextLong()) {
					amount = Optional.ofNullable(scanner.nextLong());
				} else {
					System.out.println("Wrong input, Please make sure you enter a positive number");
					scanner.nextLine();
				}
			} catch (Exception e) {
				System.out.println("Wrong input, Please make sure you enter a positive number");
				logger.error(e.getMessage());
			}
		}
		return amount;
	}
	
	public String readEmailAddress() {
		System.out.println("Please enter email address for Stripe registration");
		String emailAddress = scanner.nextLine();
		return emailAddress;
	}

	public void close() {
		this.scanner.close();
	}
}
