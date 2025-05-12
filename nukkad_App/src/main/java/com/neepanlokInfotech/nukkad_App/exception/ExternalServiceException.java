package com.neepanlokInfotech.nukkad_App.exception;

public class ExternalServiceException extends RuntimeException{
    public ExternalServiceException(String message){
        super(message);
    }

    public ExternalServiceException(String message,Throwable cause){
        super(message,cause);
    }
}
