package com.code.monks.nukkad.enums;

import lombok.Getter;

@Getter
public enum UnitEnum {
    WEIGHT(1, "KG", "GM"),
    VOLUME(2, "Litre", "ML"),
    PACKET(3, "PKT", null); // Provide null for missing third value

    private final int code;
    private final String primaryUnit;
    private final String secondaryUnit;

    UnitEnum(int code, String primaryUnit, String secondaryUnit) {
        this.code = code;
        this.primaryUnit = primaryUnit;
        this.secondaryUnit = secondaryUnit;
    }

    public static UnitEnum fromCode(int code) {
        for (UnitEnum u : values()) {
            if (u.code == code) {
                return u;
            }
        }
        throw new IllegalArgumentException("Invalid UnitEnum code: " + code);
    }
}
