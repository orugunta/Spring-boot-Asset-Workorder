package com.hari.spb.react.exception;

public class NotFoundException extends AppException {

	private static final long serialVersionUID = 1L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(AppErrors dppErrors, String... messageArguments) {
		super(dppErrors, messageArguments);
	}

	public NotFoundException(Throwable cause, AppErrors dppErrors, String... messageArguments) {
		super(cause, dppErrors, messageArguments);
	}
}
