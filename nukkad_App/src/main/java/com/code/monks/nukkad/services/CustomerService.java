package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;

	public CreateCustomerResponseDTO createCustomer(CreateCustomerRequestDTO dto){
		CustomerEntity customer = CreateCustomerRequestDTO.toEntity(dto);
		CustomerEntity saved = customerRepository.save(customer);
		return CreateCustomerResponseDTO.fromEntity(saved);
	}

	public CreateCustomerResponseDTO updateCustomer(Long id , CreateCustomerRequestDTO dto){
		CustomerEntity customer = customerRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Customer not found with id" + id));
		customer.setName(dto.getName());
		customer.setEmail(dto.getEmail());
		customer.setAddressLine1(dto.getAddressLine1());
		customer.setAddressLine2(dto.getAddressLine2());
		customer.setCity(dto.getCity());
		customer.setState(dto.getState());
		customer.setPincode(dto.getPincode());
		customer.setDob(dto.getDob());

		CustomerEntity updated = customerRepository.save(customer);
		return CreateCustomerResponseDTO.fromEntity(updated);
	}

}
