package com.code.monks.nukkad.enums;

import lombok.Getter;
import java.util.List;

@Getter
public enum OrderStatusFilterEnum {
    PENDING(List.of(OrderStatusEnum.PENDING)),
    DELIVERED(List.of(OrderStatusEnum.DELIVERED)),
    IN_PROGRESS(List.of(OrderStatusEnum.IN_PROGRESS, OrderStatusEnum.DISPATCHED));

    private final List<OrderStatusEnum> statusEnums;

    OrderStatusFilterEnum(List<OrderStatusEnum> statusEnums) {
        this.statusEnums = statusEnums;
    }
}

