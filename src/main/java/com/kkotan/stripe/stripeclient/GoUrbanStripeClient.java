package com.kkotan.stripe.stripeclient;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kkotan.stripe.stripeclient.connector.StripeConnector;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Source;

@Component
public class GoUrbanStripeClient {
	private static final Logger logger = LogManager.getLogger(GoUrbanStripeClient.class);
	private static final String CURRENCY_EUR = "eur";
	private static final String SOURCE_TYPE = "card";
	private static final String TOKEN = "tok_at";
	
	@Autowired
	private StripeConnector stripeConnector;

	public Customer registerCustomerWithCard(String emailAddress) throws StripeException {
		System.out.println("Registering customer with email address: " + emailAddress);
		Customer customer = stripeConnector.createCustomer(emailAddress);
		Source source = stripeConnector.createSourceForCustomer(TOKEN, SOURCE_TYPE, customer);
		stripeConnector.attachSource(source, customer);
		Customer customerWithSource = stripeConnector.retrieveCustomer(customer.getId());
		System.out.println("Customer succesfully registered");
		return customerWithSource;
	}
	
	public Charge chargeCustomerInEuro(Optional<Long> amount, Customer customer) throws StripeException {
		System.out.println("Creating charge of " + amount.get() + "€");
		Source source = stripeConnector.retrieveSource(customer.getSources().getData().get(0).getId());
		Charge charge = stripeConnector.createChargeForSource(amount.get(), CURRENCY_EUR, source, customer);
		System.out.println("Charge created");
		return charge;
	}
	

	public void captureOrRefundCharge(Charge charge, Optional<String> captureOrRefundLetter) {
		try {
			if (captureOrRefundLetter.get().toLowerCase().equals("c")) {
				System.out.println("Capturing charge..");
				stripeConnector.captureCharge(charge);
				System.out.println("Charge succesfully captured");
			} else if (captureOrRefundLetter.get().toLowerCase().equals("r")) {
				System.out.println("Refunding charge..");
				stripeConnector.refundCharge(charge);
				System.out.println("Charge succesfully refunded");
			} 
			else throw new IllegalArgumentException("The input didn't match C or R");
		} catch (Exception e) {
			try {
				System.out.println("Exception happened while processing request");
				logger.error(e.getMessage());
				System.out.println("Refunding charge..");
				stripeConnector.refundCharge(charge);
				System.out.println("Charge succesfully refunded");
			} catch (Exception e2) {
				System.out.println("Exception happened while refunding request. Please contact support!");
				logger.error(e2.getMessage());
			}
		}
	}
	
}
