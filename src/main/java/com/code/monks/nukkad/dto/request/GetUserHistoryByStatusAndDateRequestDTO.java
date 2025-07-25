package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.OrderStatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetUserHistoryByStatusAndDateRequestDTO {
    private OrderStatusEnum status;
    private LocalDate startDate;
    private LocalDate endDate;
}
