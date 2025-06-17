package com.code.monks.nukkad.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    CUSTOMER(1) ,
    STOREKEEPER(2);

    private final int code;

    RoleEnum(int code){
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public static RoleEnum fromCode(int code) {
        for (RoleEnum role : values()) {
            if (role.code == code) return role;
        }
        throw  new IllegalArgumentException("Invalid RoleEnum code: " + code);
        }

    }

