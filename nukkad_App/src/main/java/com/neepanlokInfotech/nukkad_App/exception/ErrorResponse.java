package com.neepanlokInfotech.nukkad_App.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int statusCode;
//    private String error;
    private String message;
}
