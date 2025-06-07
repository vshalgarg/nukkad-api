package com.code.monks.nukkad.enums;



public enum StatusEnum
{
    PENDING(1),
//    DISPATCH(2),
    DELIVERED(2),
    CANCELLED(3);

    private final int code;

    StatusEnum(int code)
    {
        this.code= code;
    }
    public int getCode()
    {
        return code;
    }
    public static StatusEnum fromCode(int code)
    {
        for (StatusEnum status : StatusEnum.values())
        {
            if(status.code== code)
            {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Status code :" +code);

    }


}
