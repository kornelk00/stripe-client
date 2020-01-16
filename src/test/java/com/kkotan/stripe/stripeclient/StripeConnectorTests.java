package com.kkotan.stripe.stripeclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
	private static final String API_KEY = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";
	private static final String CUSTOMER_EMAIL = "bob@example.com";

	@BeforeEach
	public void setApiKey() {
		StripeConnector.setApiKey(API_KEY);
	}

	@Test
	void createCustomerIsCalled_withEmail_customerIsCreatedWithCorrectParameters() throws StripeException {
		Customer customer = StripeConnector.createCustomer(CUSTOMER_EMAIL);

		assertEquals(CUSTOMER_EMAIL, customer.getEmail());
	}

	@Test
	void createCustomerIsCalled_withNoApiKey_exceptionIsThrown() throws StripeException {
		StripeConnector.setApiKey(null);

		Assertions.assertThrows(StripeClientException.class, () -> {
			StripeConnector.createCustomer(CUSTOMER_EMAIL);
		});
	}

	@Test
	void createSourceIsCalled_withCardType_sourceIsCreatedWithCardTypeAndOwner() throws StripeException {
		Customer customer = StripeConnector.createCustomer(CUSTOMER_EMAIL);
		Source source = StripeConnector.createSourceForCustomer(TOKEN, SOURCE_TYPE, customer);

		assertEquals(SOURCE_TYPE, source.getType());
		assertEquals(CUSTOMER_EMAIL, source.getOwner().getEmail());
	}

	@Test
	void createChargeIsCalled_chargeIsCreatedAccordingly() throws StripeException {
		Customer customer = StripeConnector.createCustomer(CUSTOMER_EMAIL);
		Source source = StripeConnector.createSourceForCustomer(TOKEN, SOURCE_TYPE, customer);
		Charge charge = StripeConnector.createChargeForSource(AMOUNT_2000, CURRENCY_EUR, source);

		assertFalse(charge.getCaptured());
		assertEquals(AMOUNT_2000, charge.getAmount());
		assertEquals(CURRENCY_EUR, charge.getCurrency());
	}

	@Test
	void captureChargeIsCalled_chargeIsCaptured() throws StripeException {
		Customer customer = StripeConnector.createCustomer(CUSTOMER_EMAIL);
		Source source = StripeConnector.createSourceForCustomer(TOKEN, SOURCE_TYPE, customer);
		Charge charge = StripeConnector.createChargeForSource(AMOUNT_2000, CURRENCY_EUR, source);

		assertFalse(charge.getCaptured());
		Charge capturedCharge = StripeConnector.captureCharge(charge);
		assertTrue(capturedCharge.getCaptured());
	}

	@Test
	void refundChargeIsCalled_chargeNotCapturedButRefunded() throws StripeException {
		Customer customer = StripeConnector.createCustomer(CUSTOMER_EMAIL);
		Source source = StripeConnector.createSourceForCustomer(TOKEN, SOURCE_TYPE, customer);
		Charge charge = StripeConnector.createChargeForSource(AMOUNT_2000, CURRENCY_EUR, source);

		assertFalse(charge.getCaptured());
		assertFalse(charge.getRefunded());

		Refund refund = StripeConnector.refundCharge(charge);

		assertEquals("succeeded", refund.getStatus());
		assertFalse(Charge.retrieve(charge.getId()).getCaptured());
		assertTrue(Charge.retrieve(charge.getId()).getRefunded());
	}

}
