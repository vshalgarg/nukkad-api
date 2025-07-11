package com.code.monks.nukkad.exception;

import com.code.monks.nukkad.enums.ResponseErrorCodes;
import lombok.Getter;

@Getter
public class UnhandledException extends RuntimeException {

	private final ResponseErrorCodes errorCode;

	public UnhandledException(ResponseErrorCodes errorCode, Throwable e) {
		super(e.getMessage());
		this.errorCode = errorCode;
	}

}
