package com.neepanlokInfotech.nukkad_App.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestDTO {

    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Order ID is required")
    private Long orderId;

//    @NotNull(message = "Shopkeeper ID is required")
//    private Long shopKeeperId;
}
