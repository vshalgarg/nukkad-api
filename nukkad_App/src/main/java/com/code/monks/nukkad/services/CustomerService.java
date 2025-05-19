package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNHANDLED_EXCEPTION;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerService {


	private final CustomerRepository customerRepository;


	public CreateCustomerResponseDTO saveCustomerData(CreateCustomerRequestDTO requestDTO) {
		log.info("Saving new customer: {}", requestDTO.getEmail());
		try {
			CustomerEntity customer = new CustomerEntity();
			customer.setName(requestDTO.getName());
			customer.setEmail(requestDTO.getEmail());
			customer.setAddress(requestDTO.getAddress());
			customer.setDob(requestDTO.getDob());
			CustomerEntity saved = customerRepository.save(customer);
			log.info("Customer saved with ID: {}", saved.getId());
			return CreateCustomerResponseDTO.fromDbDto(saved);
		}
		catch (Exception e){
			log.error("Unhandled exception while saving customer: {}", requestDTO, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION,e);
		}
	}


	public CreateCustomerResponseDTO updateCustomer(Long id, CreateCustomerRequestDTO requestDTO) {
		log.info("Updating customer with ID: {}", id);
		try {
			CustomerEntity customer = customerRepository.findById(id).orElseThrow(() ->
					new ResourceNotFoundException("Customer not found with id: " + id));

			customer.setName(requestDTO.getName());
			customer.setEmail(requestDTO.getEmail());
			customer.setAddress(requestDTO.getAddress());
			customer.setDob(requestDTO.getDob());

			CustomerEntity updated = customerRepository.save(customer);
			log.info("Customer updated successfully with ID: {}", updated.getId());

			return CreateCustomerResponseDTO.fromDbDto(updated);
		}
		catch (ResourceNotFoundException e) {
			log.warn("Customer not found: {}", id, e);
			throw e;
		}
		catch (Exception e) {
			log.error("Unhandled exception while updating customer: {}", requestDTO, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION,e);
		}
	}

}
