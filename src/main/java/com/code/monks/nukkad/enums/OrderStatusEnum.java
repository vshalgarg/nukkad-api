package com.code.monks.nukkad.enums;

public enum OrderStatusEnum
{
    PENDING(1),
    IN_PROGRESS(2),
    DISPATCHED(3),
    DELIVERED(4),
    CANCELLED(5);

    private final int code;

    OrderStatusEnum(int code)
    {
        this.code= code;
    }
    public int getCode()
    {
        return code;
    }
    public static OrderStatusEnum fromCode(int code)
    {
        for (OrderStatusEnum status : OrderStatusEnum.values())
        {
            if(status.code== code)
            {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Status code :" +code);

    }
}
