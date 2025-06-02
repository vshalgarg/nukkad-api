package com.code.monks.nukkad.enums;

public enum AddressLabelEnum {
    HOME(1) , OFFICE(2);

    private final int code;

    AddressLabelEnum(int code){
        this.code=code;
    }

    public int getCode(){
        return code;
    }

    public static AddressLabelEnum fromCode(int code){
        for(AddressLabelEnum status : AddressLabelEnum.values()){
            if(status.code == code){
                return status;
            }
        }
        throw new IllegalStateException("Invalid Status code: " + code);
    }
}
