package com.code.monks.nukkad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopkeeperRequestDTO {

	private String name;

	private String storeNumber;

	private String gstIn;

	private String address;

	private String city;

	private List<String> pictures;

}
