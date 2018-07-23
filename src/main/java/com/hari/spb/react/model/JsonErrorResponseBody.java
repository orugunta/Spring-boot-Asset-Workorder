package com.hari.spb.react.model;

import org.springframework.http.HttpStatus;

import com.hari.spb.react.exception.AppErrors;
import com.hari.spb.react.exception.AppException;

public class JsonErrorResponseBody {
	
	private String statusCode;
	private String errorCode;
	private String errorMessage;
	
	public JsonErrorResponseBody(HttpStatus httpStatus, Exception ex, AppErrors defaultDppError) {
		if (httpStatus != null) {
			this.statusCode = httpStatus.toString() + ": " + httpStatus.getReasonPhrase();
		}
		AppErrors dppErrors = null;
		String[] messageParameters = null;
		if (ex instanceof AppException) {
			dppErrors = ((AppException) ex).getDppErrors();
			messageParameters = ((AppException) ex).getMessageArguments();
		}
		if (dppErrors == null) {
			dppErrors = defaultDppError;
		}
		this.errorCode = dppErrors.getErrorCode();
		this.errorMessage = dppErrors.getErrorMessage(messageParameters);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getStatusCode() {
		return statusCode;
	}
	
}

