package com.neepanlokInfotech.nukkad_App.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String status)
    {
        super(status);
    }
}
