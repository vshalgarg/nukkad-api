package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.CreateStorekeeperRequestDTO;
import com.code.monks.nukkad.dto.response.CreateStorekeeperResponseDTO;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNHANDLED_EXCEPTION;

@Slf4j
@Service
@AllArgsConstructor
public class StorekeeperService {

	private final StorekeeperRepository shopkeeperRepository;

	public CreateStorekeeperResponseDTO createShopkeeper(CreateStorekeeperRequestDTO dto) {
		log.info("Creating shopkeeper with name: {}", dto.getName());
		try {
			StorekeeperEntity entity = new StorekeeperEntity();
			entity.setName(dto.getName());
			entity.setStoreNumber(dto.getStoreNumber());
			entity.setGstIn(dto.getGstIn());
			entity.setAddress(dto.getAddress());
			entity.setCity(dto.getCity());
			entity.setPictures(dto.getPictures());

			StorekeeperEntity savedEntity = shopkeeperRepository.save(entity);
			log.info("Shopkeeper created with id: {}", savedEntity.getId());

			return CreateStorekeeperResponseDTO.fromDbDto(savedEntity);
		} catch (Exception e) {
			log.error("Unhandled exception while creating shopkeeper: {}", dto, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}

	public CreateStorekeeperResponseDTO updateShopkeeper(Long id, CreateStorekeeperRequestDTO dto) {
		log.info("Updating shopkeeper with id: {}", id);
		try {
			StorekeeperEntity entity = shopkeeperRepository.findById(id).orElseThrow(() -> {
				log.error("Shopkeeper not found for update with id: {}", id);
				return new ResourceNotFoundException("Shopkeeper not found with id: " + id);
			});

			entity.setName(dto.getName());
			entity.setStoreNumber(dto.getStoreNumber());
			entity.setGstIn(dto.getGstIn());
			entity.setAddress(dto.getAddress());
			entity.setCity(dto.getCity());
			entity.setPictures(dto.getPictures());

			StorekeeperEntity updatedEntity = shopkeeperRepository.save(entity);
			log.info("Shopkeeper updated successfully with id: {}", updatedEntity.getId());

			return CreateStorekeeperResponseDTO.fromDbDto(updatedEntity);
		} catch (ResourceNotFoundException e) {
			log.warn("Shopkeeper not found for update: {}", id, e);
			throw e;
		} catch (Exception e) {
			log.error("Unhandled exception while updating shopkeeper: {}", dto, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}

}
