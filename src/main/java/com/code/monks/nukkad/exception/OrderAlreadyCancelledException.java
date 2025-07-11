package com.code.monks.nukkad.exception;

import com.code.monks.nukkad.enums.ResponseErrorCodes;
import lombok.Getter;

@Getter
public class OrderAlreadyCancelledException extends RuntimeException
{
    private final ResponseErrorCodes error;
    public OrderAlreadyCancelledException(ResponseErrorCodes error){
        super(error.getMessage());
        this.error = error;
    }
}
