package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetUserHistoryByStatusAndDateRequestDTO {
    private StatusEnum status;
    private LocalDate startDate;
    private LocalDate endDate;
}
