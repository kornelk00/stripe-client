package com.kkotan.stripe.stripeclient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import com.stripe.model.Source;

public class StripeConnector {
	private static final Logger logger = LogManager.getLogger(StripeConnector.class);

	public static void setApiKey(String apiKey) {
		Stripe.apiKey = apiKey;
	}

	public static Customer createCustomer(String emailAddress) throws StripeException {
		validateApiKey();
		logger.debug("Trying to create customer with email: " + emailAddress);
		
		Map<String, Object> params = new HashMap<>();
		params.put("email", emailAddress);
		Customer customer = Customer.create(params);
		logger.debug("Source created" );
		return customer;
	}

	public static Source createSourceForCustomer(String token, String type, Customer customer) throws StripeException {
		validateApiKey();
		logger.debug("Trying to create source with token: " + token + " type: " + type + " for customer: " + customer.getEmail());
		Map<String, Object> sourceParams = new HashMap<String, Object>();
		sourceParams.put("type", type);
		sourceParams.put("token", token);
		Map<String, Object> ownerParams = new HashMap<String, Object>();
		ownerParams.put("email", customer.getEmail());
		sourceParams.put("owner", ownerParams);
		logger.debug("Source created" );
		return Source.create(sourceParams);
	}

	public static Charge createChargeForSource(Long amount, String currency, Source source) throws StripeException {
		validateApiKey();
		logger.debug("Trying to create charge " + amount + " " + currency );
		Map<String, Object> params = new HashMap<>();
		params.put("amount", amount);
		params.put("currency", currency);
		params.put("capture", false);
		params.put("source", source.getId());
		Charge charge = Charge.create(params);
		logger.debug("Charge created" );
		return charge;
	}

	public static Charge captureCharge(Charge charge) throws StripeException {
		validateApiKey();
		logger.debug("Trying to capture charge " + charge.getId());
		Charge capturedCharge = charge.capture();
		logger.debug("Charge captured");
		return capturedCharge;
	}

	public static Refund refundCharge(Charge charge) throws StripeException {
		validateApiKey();
		logger.debug("Trying to refund charge " + charge.getId());
		Map<String, Object> params = new HashMap<>();
		params.put("charge", charge.getId());
		Refund refund = Refund.create(params);
		logger.debug("Charge refunded");
		return refund;
	}

	private static void validateApiKey() {
		Optional.ofNullable(Stripe.apiKey).orElseThrow(() -> new StripeClientException("No API key was set"));
	}
}
