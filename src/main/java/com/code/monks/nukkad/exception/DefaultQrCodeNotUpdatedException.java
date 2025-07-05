package com.code.monks.nukkad.exception;

import com.code.monks.nukkad.enums.ResponseErrorCodes;
import lombok.Getter;

@Getter
public class DefaultQrCodeNotUpdatedException extends RuntimeException{
    private final ResponseErrorCodes error;

    public DefaultQrCodeNotUpdatedException(ResponseErrorCodes error){
        super(error.getMessage());
        this.error = error;
    }
}
