package com.code.monks.nukkad.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VerifyResponseDTO {
    private String status;
    private String message;

    public VerifyResponseDTO(String status,String message)
    {
        this.status= status;
        this.message=message;
    }

    public VerifyResponseDTO(boolean b, String message) {
    }
}
