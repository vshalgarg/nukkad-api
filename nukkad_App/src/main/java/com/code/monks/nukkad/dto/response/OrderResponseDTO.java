package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.enums.StatusOrderEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponseDTO {

    private Long id;
    private String trackingNumber;
    private int quantity;
    private String status;
    private Long orderId;
    private LocalDateTime orderDate;
    private String dayName;
    private Long shopKeeperId;
    private StatusOrderEnum statusOrderEnum;

}
