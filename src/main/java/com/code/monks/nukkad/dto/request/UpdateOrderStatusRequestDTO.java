package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.Status;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDTO
{
    private Status status;
}
