package com.code.monks.nukkad.exception;

import com.code.monks.nukkad.enums.ResponseErrorCodes;
import lombok.Getter;

@Getter
public class AccessDeniedException extends RuntimeException{

    private final ResponseErrorCodes error;

    public AccessDeniedException(ResponseErrorCodes error){
        super(error.getMessage());
        this.error = error;
    }
}
