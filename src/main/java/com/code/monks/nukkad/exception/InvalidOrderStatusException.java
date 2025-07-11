package com.code.monks.nukkad.exception;

import com.code.monks.nukkad.enums.ResponseErrorCodes;
import lombok.Getter;

@Getter
public class InvalidOrderStatusException extends  RuntimeException{

    private final ResponseErrorCodes error;
    public InvalidOrderStatusException(ResponseErrorCodes error){
        super(error.getMessage());
        this.error = error;
    }
}
