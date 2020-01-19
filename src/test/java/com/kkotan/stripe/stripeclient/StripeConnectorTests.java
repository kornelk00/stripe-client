package com.kkotan.stripe.stripeclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kkotan.stripe.stripeclient.connector.StripeConnector;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import com.stripe.model.Source;

@SpringBootTest
class StripeConnectorTests {

	private static final String CURRENCY_EUR = "eur";
	private static final long AMOUNT_2000 = 2000l;
	private static final String SOURCE_TYPE = "card";
	private static final String TOKEN = "tok_at";
	private static final String CUSTOMER_EMAIL = "bob@example.com";
	
	@Autowired
	private StripeConnector stripeConnector;

	@Test
	void createCustomerIsCalled_withEmail_customerIsCreatedWithCorrectParameters() throws StripeException {
		Customer customer = stripeConnector.createCustomer(CUSTOMER_EMAIL);

		assertEquals(CUSTOMER_EMAIL, customer.getEmail());
	}

	@Test
	void createSourceIsCalled_withCardType_sourceIsCreatedWithCardTypeAndOwner() throws StripeException {
		Customer customer = stripeConnector.createCustomer(CUSTOMER_EMAIL);
		Source source = stripeConnector.createSourceForCustomer(TOKEN, SOURCE_TYPE, customer);

		assertEquals(SOURCE_TYPE, source.getType());
		assertEquals(CUSTOMER_EMAIL, source.getOwner().getEmail());
	}

	@Test
	void createChargeIsCalled_chargeIsCreatedAccordingly() throws StripeException {
		Customer customer = stripeConnector.createCustomer(CUSTOMER_EMAIL);
		Source source = stripeConnector.createSourceForCustomer(TOKEN, SOURCE_TYPE, customer);
		Charge charge = stripeConnector.createChargeForSource(AMOUNT_2000, CURRENCY_EUR, source, customer);

		assertFalse(charge.getCaptured());
		assertEquals(AMOUNT_2000*100, charge.getAmount());
		assertEquals(CURRENCY_EUR, charge.getCurrency());
	}

	@Test
	void captureChargeIsCalled_chargeIsCaptured() throws StripeException {
		Customer customer = stripeConnector.createCustomer(CUSTOMER_EMAIL);
		Source source = stripeConnector.createSourceForCustomer(TOKEN, SOURCE_TYPE, customer);
		Charge charge = stripeConnector.createChargeForSource(AMOUNT_2000, CURRENCY_EUR, source, customer);

		assertFalse(charge.getCaptured());
		Charge capturedCharge = stripeConnector.captureCharge(charge);
		assertTrue(capturedCharge.getCaptured());
	}

	@Test
	void refundChargeIsCalled_chargeNotCapturedButRefunded() throws StripeException {
		Customer customer = stripeConnector.createCustomer(CUSTOMER_EMAIL);
		Source source = stripeConnector.createSourceForCustomer(TOKEN, SOURCE_TYPE, customer);
		Charge charge = stripeConnector.createChargeForSource(AMOUNT_2000, CURRENCY_EUR, source, customer);

		assertFalse(charge.getCaptured());
		assertFalse(charge.getRefunded());

		Refund refund = stripeConnector.refundCharge(charge);

		assertEquals("succeeded", refund.getStatus());
		assertFalse(Charge.retrieve(charge.getId()).getCaptured());
		assertTrue(Charge.retrieve(charge.getId()).getRefunded());
	}

}
