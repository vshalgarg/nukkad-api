package com.code.monks.nukkad.enums;


import lombok.Getter;

@Getter
public enum UserStatusEnum {

    ACTIVE(1),
    INACTIVE(2);
    private final int value;

    UserStatusEnum(int value) {
        this.value = value;
    }
}
