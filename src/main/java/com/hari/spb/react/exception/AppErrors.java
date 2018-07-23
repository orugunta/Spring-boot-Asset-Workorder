package com.hari.spb.react.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum AppErrors {
	
	GRAPH_NOT_FOUND_VERTEX("G001", "Vertex not found"),
	ASSET_NOTFOUND("AS001", "Asset mapping not found. %s"),
	ASSET_BAD_REQUEST("AS002", "Bad request. %s"),
	WORK_ORDER_BAD_REQUEST("WO001", "Bad request. %s"),
	WORK_ORDER_NOTFOUND("WS002", "Asset mapping not found. %s"),
	DEFAULT_BAD_REQUEST("I001", "Bad request."), 
	DEFAULT_UNAUTHORIZED("I002", "Operation not authorized."), 
	DEFAULT_FORBIDDEN("I003", "Operation not authorized."), 
	DEFAULT_NOT_FOUND("I004", "Not found."), 
	DEFAULT_UNSUPPORTED_MEDIA_TYPE("I005", "Unsupported mediatype."), 
	DEFAULT_REQUEST_TIMEOUT("I006", "Timeout."), 
	DEFAULT_NO_CONTENT("I007", "No content."),
	INVALID_SUPPORTED_API_VERSION("I008", "Unsupported api-version"),
	INTERNAL_ERROR("I009", "Oops");
	
	private String errorCode;
	
	private String errorMessage;

	private static final Logger LOG = LoggerFactory.getLogger(AppErrors.class);

	private AppErrors(String errorCode, String message) {
		this.errorCode = errorCode;
		this.errorMessage = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage(String[] messageParameters) {
		if (messageParameters != null) {
			try {
				return String.format(errorMessage, (Object[]) messageParameters);
			} catch (Exception e) {
				LOG.error("{}", e);
				// To ensure that an error message is always returned
				return errorMessage;
			}
		} else {
			return errorMessage;
		}
	}

}
