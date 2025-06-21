package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.OrderRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerOrderResponseDTO;
import com.code.monks.nukkad.dto.response.CreateOrderHistoryResponseDTO;
import com.code.monks.nukkad.dto.response.CreateStoreKeeperOrderResponseDTO;
import com.code.monks.nukkad.dto.response.OrderResponseDTO;
import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.enums.Status;
import com.code.monks.nukkad.exception.OrderNotFoundException;
import com.code.monks.nukkad.repositories.AddressRepository;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.OrderRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class OrderService {  // placeOrderService
    @Autowired
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final StorekeeperRepository storekeeperRepository;


    public OrderService(OrderRepository orderRepository, AddressRepository addressRepository, CustomerRepository customerRepository, StorekeeperRepository storekeeperRepository) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.storekeeperRepository = storekeeperRepository;
    }

    public OrderResponseDTO createOrders(OrderRequestDTO requestDTO) {
        log.info("Creating new order..");
        try {
            // Convert DTO to entity without setting deliveryAddress yet
            OrderEntity orderEntity = OrderRequestDTO.toEntity(requestDTO);

            // 🔍 Validate & fetch delivery address
            Long deliveryAddressId = requestDTO.getDeliveryAddress();
            AddressEntity deliveryAddress = addressRepository.findById(deliveryAddressId)
                    .orElseThrow(() -> new EntityNotFoundException("Delivery address not found with ID: " + deliveryAddressId));

            orderEntity.setDeliveryAddress(deliveryAddress);

            // (Repeat this pattern for cart, customer, and storeKeeper if they are also passed as IDs)
            log.debug("Order Entity before save :{}", orderEntity);

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
            List<OrderEntity> orderEntities = orderRepository.findByStatus(Status.valueOf(status));
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
        } catch (IllegalArgumentException e) {
            log.error("Invalid status value:{}", status, e);
            throw new RuntimeException("Invalid status:" + status, e);
        } catch (Exception e) {
            log.error("Error while fetching orders by status", e);
            throw new RuntimeException("Failed to fetch orders by status", e);
        }
    }

//    public List<OrderResponseDTO> getAllOrderById(long id) {
//        try {
//            List<OrderEntity> orderEntityList = orderRepository.findByCustomerId((long) id);
//
//            if (orderEntityList.isEmpty()) {
//                log.warn("No orders found while fetching by id:{}", id);
//                throw new OrderNotFoundException("No orders found:" + id);
//            }
//
//            List<OrderResponseDTO> responseDTOList = new ArrayList<>();
//            for (OrderEntity order : orderEntityList) {
//                responseDTOList.add(OrderResponseDTO.toResponseDTO(order));
//            }
//            log.info("Fetching {} orders with id{}", responseDTOList.size(), id);
//            return responseDTOList;
//        } catch (Exception e) {
//            log.error("Error while fetching orders with id{} : {}", id, e.getMessage());
//            throw new RuntimeException("Failed to fetch order by id", e);
//        }
//    }

    public OrderResponseDTO cancelOrderByStoreKeeper(Long id, String storeKeeperId) {
        log.info("StoreKeeper [{}] requested to cancel order Id:{}", storeKeeperId, id);
        try {
            OrderEntity orderEntity = orderRepository.findById(id)
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));

            if (orderEntity.getStatus() == Status.PENDING) {
                log.info("Order [{}] is PENDING. Proceeding with cancellation.", id);
                orderEntity.setStatus(Status.CANCELLED);

            } else if (orderEntity.getStatus() == Status.IN_PROGRESS) {
                log.info("Order [{}] is IN_PROGRESS. Proceeding with cancellation.", id);
                orderEntity.setStatus(Status.CANCELLED);

            } else {
                log.warn("Order [{}] is in status [{}] and cannot be cancelled", id, orderEntity.getStatus());
                throw new IllegalArgumentException("Only PENDING or IN_PROGRESS orders can be cancelled by storeKeeper");
            }

            OrderEntity updatedOrder = orderRepository.save(orderEntity);
            log.info("Order [{}] cancelled successfully by storekeeper [{}]", id, storeKeeperId);
            return OrderResponseDTO.toResponseDTO(updatedOrder);

        } catch (OrderNotFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("Invalid cancellation attempt: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to cancel order [{}] by storekeeper [{}]", id, storeKeeperId, e);
            throw new RuntimeException("Failed to cancel order", e);
        }
    }

    public OrderResponseDTO updateOrderStatus(long id, OrderRequestDTO orderRequestDTO) {
        Status newStatus = orderRequestDTO.getStatus();
        log.info("Updating status for Order ID [{}] to [{}]", id, newStatus);

        try {
            // Validate input
            if (newStatus == null) {
                log.error("Status in request is null");
                throw new IllegalArgumentException("Status cannot be null");
            }

            // Fetch existing order
            OrderEntity orderEntity = orderRepository.findById(id)
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));

            // Update status
            orderEntity.setStatus(newStatus);
            OrderEntity updatedOrder = orderRepository.save(orderEntity);

            log.info("Order status updated successfully to [{}] for ID [{}]", newStatus, id);
            return OrderResponseDTO.toResponseDTO(updatedOrder);

        } catch (OrderNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update order status for ID [{}]", id, e);
            throw new RuntimeException("Failed to update order status", e);
        }
    }
    public List<CreateOrderHistoryResponseDTO> getOrderHistory(String statuss, LocalDate date) {
        log.info("Request received: Fetching orders with status='{}' and date='{}'", statuss, date);

        Status status;
        try {
            status = Status.valueOf(statuss.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid order status received: '{}'", statuss);
            throw new IllegalArgumentException("Invalid status: " + statuss);
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<OrderEntity> orderEntities = orderRepository.findByStatusAndCreatedAtBetween(status, start, end);

        if (orderEntities.isEmpty()) {
            log.warn("No orders found with status='{}' on date='{}'", status, date);
            throw new OrderNotFoundException("No orders found with status: " + statuss + " on " + date);
        }
        log.info("Found {} order(s) with status='{}' on date='{}'", orderEntities.size(), status, date);

        return orderEntities.stream()
                .map(CreateOrderHistoryResponseDTO::fromEntity)
                .collect(toList());
    }

    public List<CreateCustomerOrderResponseDTO> getOrdersByStorekeeperId(Long storeKeeperId) {
        log.info("Fetching orders for storeKeeperId={}", storeKeeperId);

        List<OrderEntity> orders = orderRepository.findByStoreKeeperId(storeKeeperId);

        // if toEntity() is static
        if (orders.isEmpty()) {
            log.warn("No orders found for storeKeeperId={}", storeKeeperId);
            throw new OrderNotFoundException("No orders found for storeKeeper ID: " + storeKeeperId);
        }

        List<CreateCustomerOrderResponseDTO> responseDTOs = orders.stream()
                .map(CreateCustomerOrderResponseDTO::toEntity) // Assuming static mapper
                .toList();

        log.info("Returning {} order(s) for storeKeeperId={}", responseDTOs.size(), storeKeeperId);
        return responseDTOs;
    }
    public List<CreateStoreKeeperOrderResponseDTO> getStoreKeeperInfoByCustomerId(Long customerId)
    {
        log.info("Fetching storekeeper info for customerId={}", customerId);

        List<OrderEntity> order = orderRepository.findByCustomerId(customerId);

        if (order.isEmpty()) {
            log.warn("No storekeeper orders found for customerId={}", customerId);
            throw new OrderNotFoundException("No orders found for customer ID: " + customerId);
        }

        List<CreateStoreKeeperOrderResponseDTO> responseDTOs = order.stream()
                .map(CreateStoreKeeperOrderResponseDTO::toEntity)
                .toList();

        log.info("Returning {} order(s) for customerId={}", responseDTOs.size(), customerId);
        return responseDTOs;
    }

}


