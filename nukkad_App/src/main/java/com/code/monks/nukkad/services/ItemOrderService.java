package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.ItemOrderRequestDTO;
import com.code.monks.nukkad.dto.response.ItemOrderResponseDTO;
import com.code.monks.nukkad.entities.ItemOrderEntity;
import com.code.monks.nukkad.repositories.ItemOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemOrderService
{
    private final ItemOrderRepository itemOrderRepository;
//
//    public ItemOrderService(ItemOrderRepository itemOrderRepository) {
//        this.itemOrderRepository = itemOrderRepository;


    public ItemOrderResponseDTO createOrder (ItemOrderRequestDTO itemOrderRequestDTO)
      {
          ItemOrderEntity savedEntity = itemOrderRepository.save(ItemOrderRequestDTO.dtoToEntity(itemOrderRequestDTO));

          return ItemOrderResponseDTO.fromDbToDto(savedEntity);
      }
      public List<ItemOrderEntity> getAllOrders()
      {
          return itemOrderRepository.findAll();
      }


}


