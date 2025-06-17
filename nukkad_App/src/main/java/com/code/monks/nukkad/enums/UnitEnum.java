package com.code.monks.nukkad.enums;

import lombok.Getter;
@Getter
public enum  UnitEnum {
    WEIGHT(1, new String[]{"KG", "GM"}),
    VOLUME(2, new String[]{"Litre", "ML"}),
    PACKET(3, new String[]{"PKT"}); // single unit as array

    private final int code;
    private final String[] units;

    UnitEnum(int code, String[] units) {
        this.code = code;
        this.units = units;
    }

    public static UnitEnum fromCode(int code) {
        for (UnitEnum u : values()) {
            if (u.code == code) return u;
        }
        throw new IllegalArgumentException("Invalid UnitEnum code: " + code);
    }
}

