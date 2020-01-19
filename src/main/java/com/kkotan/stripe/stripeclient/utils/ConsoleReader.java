package com.kkotan.stripe.stripeclient.utils;

import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsoleReader {
	private static final String REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

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

	public Optional<String> readEmailAddress() {
		System.out.println("Please enter email address for Stripe registration");
		Optional<String> emailAddress = Optional.empty();
		while (!emailAddress.isPresent()) {
			if (scanner.hasNext()) {
				emailAddress = Optional.ofNullable(scanner.nextLine());
				Pattern pattern = Pattern.compile(REGEX);

				Matcher matcher = pattern.matcher(emailAddress.get());
				if (!matcher.matches()) {
					System.out.println("The email address provided doesn't seem valid. Please provide a valid email address");
					emailAddress = Optional.empty();
				}
			}
		}
		return emailAddress;

	}

	public void close() {
		this.scanner.close();
	}
}
