package com.neepanlokInfotech.nukkad_App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class VerifyResponseDTO {
    private String status;
    private String message;

    public VerifyResponseDTO(String status,String message)
    {
        this.status= status;
        this.message=message;
    }

}
