package com.kkotan.stripe.stripeclient;

public class StripeClientException extends IllegalStateException {

	private static final long serialVersionUID = 2600794843193103271L;

	public StripeClientException() {
		super();
	}

	public StripeClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public StripeClientException(String s) {
		super(s);
	}

	public StripeClientException(Throwable cause) {
		super(cause);
	}

}
