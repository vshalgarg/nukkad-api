package com.code.monks.nukkad.enums;

import lombok.Getter;
@Getter
public enum  UnitEnum {
    WEIGHT(1, new String[]{"KG", "GM"}),
    VOLUME(2, new String[]{"L", "ML"}),
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

    public String toUpperCaseUnit() {
        return (units != null && units.length > 0) ? units[0].toUpperCase() : "";
    }

    public static boolean isValidUnit(String unit) {
        if (unit == null) return false;
        String upperUnit = unit.toUpperCase();
        for (UnitEnum u : values()) {
            for (String allowed : u.getUnits()) {
                if (allowed.equalsIgnoreCase(upperUnit)) return true;
            }
        }
        return false;
    }

}

