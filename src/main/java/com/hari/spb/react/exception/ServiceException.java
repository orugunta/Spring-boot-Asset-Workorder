package com.hari.spb.react.exception;

public class ServiceException extends AppException {

	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}

	public ServiceException(AppErrors dppErrors, String... messageArguments) {
		super(dppErrors, messageArguments);
	}

	public ServiceException(Throwable cause, AppErrors dppErrors, String... messageArguments) {
		super(cause, dppErrors, messageArguments);
	}
}
