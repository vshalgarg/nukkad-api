package com.code.monks.nukkad.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExceptionLogRequestDTO {
    @NotBlank(message = "Message is required")
    private String message;

    @NotBlank(message = "Request path is required")
    private String requestPath;
}
