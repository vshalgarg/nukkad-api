package com.code.monks.nukkad.exception;

import com.code.monks.nukkad.enums.ResponseErrorCodes;
import lombok.Getter;

@Getter
public class StorekeeperCreationException extends RuntimeException{
    private final ResponseErrorCodes error;

    public StorekeeperCreationException(ResponseErrorCodes error, Throwable cause){
        super(cause.getMessage());
        this.error = error;
    }
}
