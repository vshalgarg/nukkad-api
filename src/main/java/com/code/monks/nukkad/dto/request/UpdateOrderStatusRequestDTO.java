package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.StatusEnum;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDTO
{
    private StatusEnum orderStatus;
}
