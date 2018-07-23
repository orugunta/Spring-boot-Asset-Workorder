package com.hari.spb.react.exception;

public class TimeoutException extends AppException {

	private static final long serialVersionUID = 1L;

	public TimeoutException() {
		super();
	}

	public TimeoutException(AppErrors dppErrors, String... messageArguments) {
		super(dppErrors, messageArguments);
	}

	public TimeoutException(Throwable cause, AppErrors dppErrors, String... messageArguments) {
		super(cause, dppErrors, messageArguments);
	}
}
