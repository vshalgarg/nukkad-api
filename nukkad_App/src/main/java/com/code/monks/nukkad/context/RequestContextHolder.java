package com.code.monks.nukkad.context;

import com.code.monks.nukkad.dto.Customer;

public class  RequestContextHolder {
    private static final ThreadLocal<Customer> tokenHolder = new ThreadLocal<>();

    public static void setCustomer(Customer customer){
        tokenHolder.set(customer);
    }

    public static Customer getCustomer(){
        return tokenHolder.get();
    }

    public static void clear(){
        tokenHolder.remove();
    }
}
