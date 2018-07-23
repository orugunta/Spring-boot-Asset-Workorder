package com.hari.spb.react.exception;

public class AppException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final AppErrors dppErrors;
	
	private final String[] messageArguments;
	
	public AppException() {
		super();
		dppErrors = null;
		messageArguments = null;
	}

	public AppException(Throwable cause, AppErrors dppErrors, String... messageArguments) {
		super(dppErrors.getErrorCode(), cause);
		this.dppErrors = dppErrors;
		this.messageArguments = messageArguments;
	}

	
	public AppException(AppErrors dppErrors, String... messageArguments) {
		super();
		this.dppErrors = dppErrors;
		this.messageArguments = messageArguments;
	}
	
	public AppErrors getDppErrors() {
		return dppErrors;
	}
	
	public String[] getMessageArguments() {
		return messageArguments;
	}
}
