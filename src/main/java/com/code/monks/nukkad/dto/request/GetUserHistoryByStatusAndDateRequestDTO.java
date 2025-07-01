package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetUserHistoryByStatusAndDateRequestDTO {
    private Status status;
    private LocalDate startDate;
    private LocalDate endDate;
}
