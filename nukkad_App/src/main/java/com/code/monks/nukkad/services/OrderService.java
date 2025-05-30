//package com.code.monks.nukkad.services;
//
//import com.code.monks.nukkad.dto.request.OrderRequestDTO;
//import com.code.monks.nukkad.dto.response.OrderResponseDTO;
//import com.code.monks.nukkad.entities.OrderEntity;
//import com.code.monks.nukkad.enums.StatusOrderEnum;
//import com.code.monks.nukkad.exception.OrderNotFoundException;
////import com.code.monks.nukkad.mapper.OrderMapper;
//import com.code.monks.nukkad.repositories.OrderRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@Slf4j
//public class OrderService {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    public OrderService(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }
//
//    public OrderResponseDTO createDTO(OrderRequestDTO requestDTO) {
//        log.info("Creating new order with tracking number:{}", requestDTO.getTrackingNumber());
//        try {
//            OrderEntity orderEntity = OrderMapper.toEntity(requestDTO);
//            if(requestDTO.getStatus().equalsIgnoreCase("dispatch"))
//            {
//                orderEntity.setStatusOrderEnum(StatusOrderEnum.DISPATCH);
//            } else if (requestDTO.getStatus().equalsIgnoreCase("cancelled")) {
//                orderEntity.setStatusOrderEnum(StatusOrderEnum.CANCELLED);
//            } else if (requestDTO.getStatus().equalsIgnoreCase("delivered")) {
//                orderEntity.setStatusOrderEnum(StatusOrderEnum.DELIVERED);
//            }
//            else
//            orderEntity.setStatusOrderEnum(StatusOrderEnum.PENDING);
//            OrderEntity orderSaved = orderRepository.save(orderEntity);
//
//            log.info("Order saved successfully with ID:{}", orderSaved.getId());
//
//            return OrderMapper.toResponseDTO(orderSaved);
//        } catch (Exception e) {
//            log.error("Failed to create order", e);
//
//            throw new RuntimeException("Failed to create order", e);
//        }
//    }
//
//    public List<OrderResponseDTO> getOrderByStatus(String status) {
//        log.info("Fetching orders with status:{}", status);
//        try {
//            List<OrderEntity> orderEntities = orderRepository.findByStatusIgnoreCase(status);
//            if (orderEntities.isEmpty()) {
//                log.warn("No orders found with status:{}", status);
//
//                throw new OrderNotFoundException("No orders found with status: " + status);
//            }
//            List<OrderResponseDTO> responseDTOList = new ArrayList<>();
//
//            for (OrderEntity orderEntity : orderEntities) {
//                responseDTOList.add(OrderMapper.toResponseDTO(orderEntity));
//
////            OrderEntity orderEntity = null;
//                responseDTOList.add(OrderMapper.toResponseDTO(orderEntity));
//            }
//            log.info("Found orders with status:{}", responseDTOList.size(), status);
//
//            return responseDTOList;
//        } catch (EntityNotFoundException e) {
//            throw e;
//        } catch (Exception e) {
//            log.error("Error while fetching orders by status", e);
//            throw new RuntimeException("Failed to fetch orders by status", e);
//        }
//    }
//
//    public List<OrderResponseDTO> getOrderByTrackingNumber(String trackingNumber) {
//        log.info("Fetching orders with tracking number:{}", trackingNumber);
//        List<OrderEntity> orderEntities = orderRepository.findByTrackingNumberIgnoreCase(trackingNumber);
//        if (orderEntities.isEmpty()) {
//            log.warn("No  orders found with tracking number:{}", trackingNumber);
//            throw new OrderNotFoundException("No orders found with tracking number:" + trackingNumber);
//        }
//        List<OrderResponseDTO> responseDTOS = new ArrayList<>();
//        for (OrderEntity orderEntity : orderEntities) {
//            responseDTOS.add(OrderMapper.toResponseDTO(orderEntity));
//        }
//        return responseDTOS;
//    }
//}