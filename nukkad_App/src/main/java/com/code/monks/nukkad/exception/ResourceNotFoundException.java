package com.code.monks.nukkad.exception;

import com.code.monks.nukkad.enums.ResponseErrorCodes;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

   private final ResponseErrorCodes error;

	public ResourceNotFoundException(ResponseErrorCodes error){

		super(error.getMessage());
		this.error = error;
	}
	public ResourceNotFoundException(ResponseErrorCodes error,Long id){

		super(error.getMessage() + id);
		this.error = error;
	}

}
