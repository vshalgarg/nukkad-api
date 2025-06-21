package com.code.monks.nukkad.enums;

public enum Status
{
    PENDING(1),
    IN_PROGRESS(2),
    DISPATCH(3),
    DELIVERED(4),
    CANCELLED(5);

    private final int code;

    Status(int code)
    {
        this.code= code;
    }
    public int getCode()
    {
        return code;
    }
    public static Status fromCode(int code)
    {
        for (Status status : Status.values())
        {
            if(status.code== code)
            {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Status code :" +code);

    }


    public void setId(Status status) {

    }
}
