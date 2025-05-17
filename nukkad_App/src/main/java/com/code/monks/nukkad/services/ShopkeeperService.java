package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.ShopkeeperRequestDTO;
import com.code.monks.nukkad.dto.ShopkeeperResponseDTO;
import com.code.monks.nukkad.entities.ShopkeeperEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.mapper.ShopkeeperMapper;
import com.code.monks.nukkad.repositories.ShopkeeperRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShopkeeperService {

	@Autowired
	private ShopkeeperRepository shopkeeperRepository;

	public ShopkeeperResponseDTO createShopkeeper(ShopkeeperRequestDTO dto) {
		log.info("Creating shopkeeper with name: {}", dto.getName());

		ShopkeeperEntity entity = ShopkeeperMapper.toEntity(dto);
		ShopkeeperEntity savedEntity = shopkeeperRepository.save(entity);

		log.info("Shopkeeper created with id: {}", savedEntity.getId());
		return ShopkeeperMapper.toDTO(savedEntity);
	}

	public ShopkeeperResponseDTO getById(Long id) {
		log.info("Fetching shopkeeper with id: {}", id);

		ShopkeeperEntity entity = shopkeeperRepository.findById(id).orElseThrow(() -> {
			log.error("Shopkeeper not found with id: {}", id);
			return new ResourceNotFoundException("Shopkeeper not found with id: " + id);
		});
		return ShopkeeperMapper.toDTO(entity);
	}

	public ShopkeeperResponseDTO updateShopkeeper(Long id, ShopkeeperRequestDTO dto) {
		log.info("Updating shopkeeper with id: {}", id);
		ShopkeeperEntity entity = shopkeeperRepository.findById(id).orElseThrow(() -> {
			log.error("Shopkeeper not found for update with id: {}", id);
			return new ResourceNotFoundException("Shopkeeper not found with id: " + id);
		});

		entity.setName(dto.getName());
		entity.setStoreNumber(dto.getStoreNumber());
		entity.setGstIn(dto.getGstIn());
		entity.setAddress(dto.getAddress());
		entity.setCity(dto.getCity());
		entity.setPictures(dto.getPictures());

		ShopkeeperEntity updatedEntity = shopkeeperRepository.save(entity);
		log.info("Shopkeeper updated successfully with id: {}", updatedEntity.getId());

		return ShopkeeperMapper.toDTO(updatedEntity);
	}

}
