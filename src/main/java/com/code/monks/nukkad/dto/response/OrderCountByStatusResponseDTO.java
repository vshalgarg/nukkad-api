package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.enums.OrderStatusEnum;
import com.code.monks.nukkad.enums.RoleEnum;
import lombok.Data;

@Data
public class OrderCountByStatusResponseDTO {

    private Long userId;
    private RoleEnum userRole;
    private OrderStatusEnum status;
    private long count;
    private String message;
}
