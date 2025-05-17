package com.code.monks.nukkad.mapper;

import com.code.monks.nukkad.dto.CustomerRequestDTO;
import com.code.monks.nukkad.dto.CustomerResponseDTO;
import com.code.monks.nukkad.entities.CustomerEntity;

public class CustomerMapper {

	/*
	 *
	 * Convert Entity to DTO
	 *
	 */

	public static CustomerResponseDTO customerResponseDTO(CustomerEntity customerEntity) {
		CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
		customerResponseDTO.setId(customerEntity.getId());
		customerResponseDTO.setName(customerEntity.getName());
		customerResponseDTO.setEmail(customerEntity.getEmail());
		customerResponseDTO.setAddress(customerEntity.getAddress());
		customerResponseDTO.setDob(customerEntity.getDob());
		return customerResponseDTO;
	}

	/*
	 *
	 * Convert DTO to Entity
	 *
	 */

	public static CustomerEntity customerEntity(CustomerRequestDTO customerRequestDTO) {
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setName(customerRequestDTO.getName());
		customerEntity.setEmail((customerRequestDTO.getEmail()));
		customerEntity.setAddress(customerRequestDTO.getAddress());
		customerEntity.setDob((customerRequestDTO.getDob()));
		return customerEntity;
	}

}
