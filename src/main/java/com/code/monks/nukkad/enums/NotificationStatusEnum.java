package com.code.monks.nukkad.enums;

import jakarta.persistence.Converter;
import lombok.Getter;

@Getter
public enum NotificationStatusEnum {

    ON(1),
    OFF(0);


    private final int code;

    NotificationStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static NotificationStatusEnum fromCode(int code) {
        for (NotificationStatusEnum status : NotificationStatusEnum.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid NotificationStatus code: " + code);
    }
}
