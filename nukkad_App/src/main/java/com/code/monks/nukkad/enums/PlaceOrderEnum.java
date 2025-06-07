package com.code.monks.nukkad.enums;

public enum PlaceOrderEnum
{
    PENDING(1),
    IN_PROGRESS(2),
    COMPLETED(3);


    private final int code;

    PlaceOrderEnum(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

    public static PlaceOrderEnum fromCode(int code)
    {
        for(PlaceOrderEnum status :PlaceOrderEnum.values())
        {
            if(status.code == code)
            {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Status code:" + code);
    }
}
