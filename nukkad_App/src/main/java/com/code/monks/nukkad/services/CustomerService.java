package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.CustomerRequestDTO;
import com.code.monks.nukkad.dto.CustomerResponseDTO;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.mapper.CustomerMapper;
import com.code.monks.nukkad.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	/*
	 *
	 * To save customer data
	 *
	 */
	public CustomerResponseDTO saveCustomerData(CustomerRequestDTO customerRequestDTO) {
		log.info("Saving new customer: {}", customerRequestDTO.getEmail());

		CustomerEntity customerEntity = CustomerMapper.customerEntity(customerRequestDTO);

		log.debug("Mapped CustomerEntity: {}", customerEntity);

		CustomerEntity saved = customerRepository.save(customerEntity);

		log.info("Customer saved with ID: {}", saved.getId());

		CustomerResponseDTO customerResponseDTO = CustomerMapper.customerResponseDTO(saved);
		log.debug("Returning CustomerResponseDTO: {}", customerResponseDTO);

		return customerResponseDTO;

		// catch(Exception e){
		// log.error("Error occurred while saving customer", e);
		// // Wrap the original exception in ValidationException to trigger the handler
		// throw new BadRequestException("Failed to save customer due to invalid data.");
		// }
	}

	/*
	 *
	 * To update customer data
	 *
	 */

	public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO customerRequestDTO) {
		log.info("Updating customer with ID: {}", id);

		CustomerEntity existingCustomer = customerRepository.findById(id).orElseThrow(() -> {
			log.warn("Customer not found with ID: {}", id);
			return new ResourceNotFoundException("Customer not found with id: " + id);
		});
		log.debug("Existing customer data: {}", existingCustomer);
		// Update the fields
		existingCustomer.setName(customerRequestDTO.getName());
		existingCustomer.setEmail(customerRequestDTO.getEmail());
		existingCustomer.setAddress(customerRequestDTO.getAddress());
		existingCustomer.setDob(customerRequestDTO.getDob());

		log.debug("Updated customer entity before saving: {}", existingCustomer);
		// Save updated entity
		CustomerEntity updatedCustomer = customerRepository.save(existingCustomer);
		log.info("Customer updated successfully with ID: {}", updatedCustomer.getId());

		// Convert to ResponseDTO
		CustomerResponseDTO customerResponseDTO = CustomerMapper.customerResponseDTO(updatedCustomer);
		log.debug("Returning CustomerResponseDTO: {}", customerResponseDTO);

		return customerResponseDTO;
	}

}
