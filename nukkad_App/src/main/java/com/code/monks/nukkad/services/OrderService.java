package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.OrderRequestDTO;
import com.code.monks.nukkad.dto.response.OrderResponseDTO;
import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.StatusEnum;
import com.code.monks.nukkad.exception.OrderNotFoundException;
import com.code.monks.nukkad.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderService {  // placeOrderService

    @Autowired
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    public OrderResponseDTO createDTO(OrderRequestDTO requestDTO) {
        try {
            OrderEntity orderEntity = OrderRequestDTO.toEntity(requestDTO);

            OrderEntity orderSaved = orderRepository.save(orderEntity);
            log.info("Order saved at: {}, updated at: {}", orderSaved.getCreatedAt(), orderSaved.getUpdatedAt());

            log.info("Order saved successfully with ID:{}", orderSaved.getId());

            return OrderResponseDTO.toResponseDTO(orderSaved);
        } catch (Exception e) {
            log.error("Failed to create order", e);

            throw new RuntimeException("Failed to create order", e);
        }
    }

    public List<OrderResponseDTO> getOrderByStatus(String status) {
        log.info("Fetching orders with status:{}", status);
        try {
            List<OrderEntity> orderEntities = orderRepository.findByStatusEnum(StatusEnum.valueOf(status));
            if (orderEntities.isEmpty()) {
                log.warn("No orders found with status:{}", status);

//                Exception e = null;
                throw new OrderNotFoundException("No orders found with status: " + status);
            }
            List<OrderResponseDTO> responseDTOList = new ArrayList<>();

            for (OrderEntity orderEntity : orderEntities) {
                responseDTOList.add(OrderResponseDTO.toResponseDTO(orderEntity));

            }
            log.info("Found orders with status:{}", responseDTOList.size(), status);

            return responseDTOList;
        } catch (IllegalArgumentException e)
        {
            log.error("Invalid status value:{}" , status, e);
            throw new RuntimeException("Invalid status:"+ status,e);
        }
        catch (Exception e) {
            log.error("Error while fetching orders by status", e);
            throw new RuntimeException("Failed to fetch orders by status", e);
        }
    }
    public List<OrderResponseDTO> getAllOrderById(int id)
    {
        try
        {
             List<OrderEntity> orderEntityList = orderRepository.findByCustomerId(id);

             if(orderEntityList.isEmpty())
             {
                 log.warn("No orders found while fetching by id:{}",id);
                 throw new OrderNotFoundException("No orders found:"+ id);
             }

             List<OrderResponseDTO> responseDTOList = new ArrayList<>();
             for (OrderEntity order : orderEntityList)
             {
                 responseDTOList.add(OrderResponseDTO.toResponseDTO(order));
             }
             log.info("Fetching {} orders with id" , responseDTOList.size(),id);
            return responseDTOList;
        }
        catch (Exception e)
        {
            log.error("Error while fetching orders with id : {}", e);
            throw new RuntimeException("Failed to fetch order by id" , e);
        }
    }
    }
//
//    public OrderResponseDTO getOrderById(int id) {
//        try {
//            OrderEntity orderEntity = orderRepository.findById(id)
//                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
//            return OrderResponseDTO.toResponseDTO(orderEntity);
//        } catch (OrderNotFoundException e) {
//            throw e; // handled by global handler
//        } catch (Exception e) {
//            log.error("Failed to fetch order by ID: {}", id, e);
//            throw new RuntimeException("Failed to fetch order by ID: " + id, e);
//        }
//    }

//    public OrderResponseDTO getOrderByCartId(int cartId) {
//        log.info("Fetching order with cartId: {}", cartId);
//        try {
//            OrderEntity orderEntity = orderRepository.findByCartId(cartId)
//                    .orElseThrow(() -> new OrderNotFoundException("Order not found with cartId: " + cartId));
//
//            return OrderResponseDTO.toResponseDTO(orderEntity);
//        } catch (OrderNotFoundException e) {
//            throw e;
//        } catch (Exception e) {
//            log.error("Failed to fetch order by cartId", e);
//            throw new RuntimeException("Failed to fetch order by cartId", e);
//        }
//    }
