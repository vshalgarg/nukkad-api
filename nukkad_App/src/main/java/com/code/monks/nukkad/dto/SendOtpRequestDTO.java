package com.code.monks.nukkad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendOtpRequestDTO {

	public String name;

	private String mobileNumber;

}
