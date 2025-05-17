package com.code.monks.nukkad.exception;

public class ExternalServiceException extends RuntimeException {

	public ExternalServiceException(String message) {
		super(message);
	}

	public ExternalServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
