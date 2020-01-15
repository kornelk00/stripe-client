package com.kkotan.stripe.stripeclient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StripeClientApplicationTests {

	@Test
	void contextLoads() {
		String apiKey = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";
		StripeConnector stripeConnector = new StripeConnector(apiKey );
	}

}
