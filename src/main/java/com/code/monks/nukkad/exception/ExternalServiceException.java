package com.code.monks.nukkad.exception;

import com.code.monks.nukkad.enums.ResponseErrorCodes;
import lombok.Getter;

@Getter
public class ExternalServiceException extends RuntimeException {

	private final ResponseErrorCodes error;

	public ExternalServiceException(String message) {
		super(message);
		this.error = null;
	}

	public ExternalServiceException(String message, Throwable cause) {
		super(message, cause);
		this.error = null;
	}

	public ExternalServiceException(ResponseErrorCodes error , String message) {
		super(message);
		this.error = error;
	}

	public ExternalServiceException(ResponseErrorCodes error, Throwable cause) {
		super(error.getMessage(), cause);
		this.error = error;
	}
}
