package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final StorekeeperRepository storekeeperRepository;

	public CreateCustomerResponseDTO createCustomer(CreateCustomerRequestDTO dto){
		Long userId = UserContextHolder.getUser().getId();
		CustomerEntity customer = CreateCustomerRequestDTO.toEntity(dto);
		customer.setId(userId);
		CustomerEntity saved = customerRepository.save(customer);
		return CreateCustomerResponseDTO.fromEntity(saved);
	}

	public CreateCustomerResponseDTO updateCustomer( CreateCustomerRequestDTO dto){
		Long userId = UserContextHolder.getUser().getId();
		CustomerEntity customer = customerRepository.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("Customer not found with id" + userId));
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

	public String addStoreToCustomer(Long customerId, String storeId) {
		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		StorekeeperEntity storekeeper = storekeeperRepository.findByStoreId(storeId)
				.orElseThrow(() -> new RuntimeException(STR."Storekeeper not found with storeId: \{storeId}"));

		if (!customer.getStorekeepers().contains(storekeeper)) {
			customer.getStorekeepers().add(storekeeper);
			customerRepository.save(customer);
			return "Store added to customer.";
		} else {
			return "Store already added.";
		}
	}


	public List<StorekeeperEntity> getMyStores(Long customerId) {
		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));
		return customer.getStorekeepers();
	}

	public String deleteStoreFromCustomer(Long customerId, String storeId) {
		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		StorekeeperEntity storekeeper = storekeeperRepository.findByStoreId(storeId)
				.orElseThrow(() -> new RuntimeException(STR."Storekeeper not found with storeId: \{storeId}"));

		if (customer.getStorekeepers().contains(storekeeper)) {
			customer.getStorekeepers().remove(storekeeper);
			customerRepository.save(customer);
			return "Store removed from customer.";
		} else {
			return "Store not associated with customer.";
		}
	}

	public String generateUniqueStoreId() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String storeId;
		do {
			storeId = STR."\{chars.charAt((int) (Math.random() * chars.length()))}\{chars.charAt((int) (Math.random() * chars.length()))}\{chars.charAt((int) (Math.random() * chars.length()))}";
		} while (storekeeperRepository.existsByStoreId(storeId));
		return storeId;
	}
}
