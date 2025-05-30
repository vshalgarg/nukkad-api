//package com.code.monks.nukkad.services;
//
//import com.code.monks.nukkad.dto.request.PlaceOrderRequestDTO;
//import com.code.monks.nukkad.dto.response.PlaceOrderResponseDTO;
//import com.code.monks.nukkad.entities.PlaceOrderEntity;
//import com.code.monks.nukkad.enums.ResponseErrorCodes;
//import com.code.monks.nukkad.exception.DuplicateResourceException;
//import com.code.monks.nukkad.exception.ResourceNotFoundException;
//import com.code.monks.nukkad.repositories.PlaceOrderRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class PlaceOrderService
//{
//    private final PlaceOrderRepository placeOrderRepository;
//
//    public PlaceOrderResponseDTO createOrder (PlaceOrderRequestDTO placeOrderRequestDTO)
//      {
//          try {
//              log.info("Creating new order for: {}");
//              PlaceOrderEntity entity=PlaceOrderRequestDTO.dtoToEntity(placeOrderRequestDTO);
//              PlaceOrderEntity savedEntity = placeOrderRepository.save(entity);
//              log.info("Order created with ID: {}", savedEntity.getId());
//              return PlaceOrderResponseDTO.fromDbToDto(savedEntity);
//          }catch (DataIntegrityViolationException ex){
//              log.error("Duplicate entry while saving order: {}", ex.getMessage());
//
//              throw new DuplicateResourceException(ResponseErrorCodes.DUPLICATE_CATEGORY_EXCEPTION);
//          }
//
//      }
//      public List<PlaceOrderEntity> getAllOrders()
//
//      {
//          log.info("Fetching all orders");
//
//          return placeOrderRepository.findAll();
//      }
//
//      public PlaceOrderEntity getOrderById(Long id)
//      {
//          log.info("Fetching order with ID: {}", id);
//
//          return placeOrderRepository.findById(id).orElseThrow();
//      }
//
//      public PlaceOrderEntity deleteOrder(Long id)
//      {
//          log.info("Deleting order with ID: {}", id);
//
//          if (!placeOrderRepository.existsById(id)) {
//              log.warn("Attempted to delete non-existing order with ID: {}", id);
//              throw new ResourceNotFoundException("Order not found with ID: " + id);
//          }
//          placeOrderRepository.deleteById(id);
//          log.info("Order deleted with ID: {}", id);
//
//          return null;
//      }
//}
//
//
