package com.neepanlokInfotech.nukkad_App.mapper;

import com.neepanlokInfotech.nukkad_App.dto.ShopkeeperRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.ShopkeeperResponseDTO;
import com.neepanlokInfotech.nukkad_App.entities.ShopkeeperEntity;

public class ShopkeeperMapper {


        public static ShopkeeperEntity toEntity(ShopkeeperRequestDTO dto) {
            ShopkeeperEntity entity = new ShopkeeperEntity();
            entity.setName(dto.getName());
            entity.setStoreNumber(dto.getStoreNumber());
            entity.setGstIn(dto.getGstIn());
            entity.setAddress(dto.getAddress());
            entity.setCity(dto.getCity());
            entity.setPictures(dto.getPictures());
            return entity;
        }

        public static ShopkeeperResponseDTO toDTO(ShopkeeperEntity entity) {
            ShopkeeperResponseDTO dto = new ShopkeeperResponseDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setStoreNumber(entity.getStoreNumber());
            dto.setGstIn(entity.getGstIn());
            dto.setAddress(entity.getAddress());
            dto.setCity(entity.getCity());
            dto.setPictures(entity.getPictures());
            return dto;
        }
    }


