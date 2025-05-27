package com.code.monks.nukkad.enums;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusEnum
{
    PENDING ,DISPATCH, DELIVERED, CANCELLED;

    @JsonCreator
    public static StatusEnum fromString(String value) {
        return StatusEnum.valueOf(value.toUpperCase());
    }

}
