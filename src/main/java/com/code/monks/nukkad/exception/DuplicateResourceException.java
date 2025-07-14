package com.code.monks.nukkad.exception;

import com.code.monks.nukkad.enums.ResponseErrorCodes;
import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {

	private final ResponseErrorCodes error;

	public DuplicateResourceException(ResponseErrorCodes error) {
		super(error.getMessage());
		this.error = error;
	}

	public DuplicateResourceException(ResponseErrorCodes error,String st){
		super(st);
		this.error = error;
	}
	public DuplicateResourceException(ResponseErrorCodes error,Throwable e){
		super(e.getMessage());
		this.error = error;
	}

}
