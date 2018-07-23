package com.hari.spb.react.exception;

public class BadRequestException extends AppException {

	private static final long serialVersionUID = 1L;

	public BadRequestException() {
		super();
	}

	public BadRequestException(AppErrors dppErrors, String... messageArguments) {
		super(dppErrors, messageArguments);
	}

	public BadRequestException(Throwable cause, AppErrors dppErrors, String... messageArguments) {
		super(cause, dppErrors, messageArguments);
	}
}
