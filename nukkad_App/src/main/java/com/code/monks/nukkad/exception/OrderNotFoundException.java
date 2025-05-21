package com.code.monks.nukkad.exception;

public class OrderNotFoundException  extends RuntimeException
{
    public OrderNotFoundException(String status)
    {
      super(status);
    }


}
