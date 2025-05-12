package com.neepanlokInfotech.nukkad_App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String dob;
}
