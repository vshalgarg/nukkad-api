package com.code.monks.nukkad.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    PENDING(1),
    IN_PROGRESS(2),
    COMPLETED(3);
//     CANCELLED(4);

    private final int code;

    OrderStatusEnum(int code) {
        this.code = code;
    }

    public static OrderStatusEnum fromCode(int code) {
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
}
