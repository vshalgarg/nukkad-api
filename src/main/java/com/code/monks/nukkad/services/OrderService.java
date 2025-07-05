
package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.DispatchOrderRequestDTO;
import com.code.monks.nukkad.dto.request.OrderRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateOrderStatusRequestDTO;
import com.code.monks.nukkad.dto.response.*;
import com.code.monks.nukkad.entities.*;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.enums.Status;
import com.code.monks.nukkad.exception.OrderNotFoundException;
import com.code.monks.nukkad.exception.UnauthorizedAccessException;
import com.code.monks.nukkad.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class OrderService { // placeOrderService

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final StorekeeperRepository storekeeperRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartProductRepository cartProductRepository;


    public OrderService(OrderRepository orderRepository, AddressRepository addressRepository, CustomerRepository customerRepository,
                        StorekeeperRepository storekeeperRepository, OrderItemRepository orderItemRepository,
                        CartProductRepository cartProductRepository) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.storekeeperRepository = storekeeperRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartProductRepository = cartProductRepository;
    }

    public PlaceOrderResponseDTO placeOrders(OrderRequestDTO requestDTO) {
        log.info("[CREATE ORDER] Request received for customer. Payload: {}", requestDTO);

        try {
            Long customerId = UserContextHolder.getUser().getId();
            log.debug("[CREATE ORDER] Authenticated customer ID: {}", customerId);

            // Convert DTO to entity
            OrderEntity orderEntity = OrderRequestDTO.toEntity(requestDTO);
            log.debug("[CREATE ORDER] Mapped OrderEntity: {}", orderEntity);

            // Set timestamps
            LocalDateTime now = LocalDateTime.now();
            orderEntity.setCreatedAt(now);
            orderEntity.setUpdatedAt(now);

            // Fetch customer
            CustomerEntity customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));
            orderEntity.setCustomer(customer);
            log.debug("[CREATE ORDER] Customer found: {}", customer.getName());

            boolean isFirstOrder = orderRepository.countByCustomerId(customerId) == 0;
            if (isFirstOrder) {
                orderEntity.setStatus(Status.PENDING);
                log.debug("[CREATE ORDER] First order for customer. Setting status to PENDING.");
            }
            // Fetch delivery address
            AddressEntity deliveryAddress = addressRepository.findById(requestDTO.getDeliveryAddressId())
                    .orElseThrow(() -> new EntityNotFoundException("Delivery address not found with ID: " + requestDTO.getDeliveryAddressId()));
            orderEntity.setDeliveryAddress(deliveryAddress);
            log.debug("[CREATE ORDER] Delivery address set: {}", deliveryAddress.getAddressLine1());

            // Fetch cart items
            List<CartProductEntity> cartItems = cartProductRepository.findByCustomerId(customerId);
            log.debug("[CREATE ORDER] Found {} cart item(s) for customerId={}", cartItems.size(), customerId);

            // Convert cart items to order items
            List<OrderItemEntity> orderItems = cartItems.stream().map(cartItem -> {
                OrderItemEntity orderItem = new OrderItemEntity();

                orderItem.setItem(cartItem.getItem());
                orderItem.setItemName(cartItem.getItem().getName());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setUnit(cartItem.getUnit());
                orderItem.setOrders(orderEntity);
                log.debug("[CREATE ORDER] Converted cart item to order item: itemId={}, quantity={}",
                        cartItem.getItem().getId(), cartItem.getQuantity());
                return orderItem;
            }).toList();

         orderEntity.setOrderItems(orderItems);
            // Save order
            orderRepository.save(orderEntity);
            log.info("[CREATE ORDER] Order saved successfully with ID: {}", orderEntity.getId());

            // Return success response
            PlaceOrderResponseDTO responseDTO = new PlaceOrderResponseDTO();
            responseDTO.setMessage("Order placed successfully");
            return responseDTO;

        } catch (Exception e) {
            log.error("[CREATE ORDER] Failed to create order for request: {}", requestDTO, e);
            throw new RuntimeException("Failed to create order", e);
        }
    }

    public CancelOrderByStoreKeeperResponseDTO cancelOrderByStoreKeeper(Long id) {
        Long storekeeperId = UserContextHolder.getUser().getId();

        log.info("StoreKeeper [{}] requested to cancel order Id:{}", storekeeperId, id);
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
            log.info("Order [{}] cancelled successfully by storekeeper [{}]", id, storekeeperId);
            return  new CancelOrderByStoreKeeperResponseDTO("Order are cancelled successfully !!");

        } catch (OrderNotFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("Invalid cancellation attempt: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to cancel order [{}] by storekeeper [{}]", id, storekeeperId, e);
            throw new RuntimeException("Failed to cancel order", e);
        }
    }

    public UpdateOrderStatusResponseDTO updateOrderStatus(long id, UpdateOrderStatusRequestDTO updateOrderStatusRequestDTO) {
        Status newStatus = updateOrderStatusRequestDTO.getStatus();
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

            if (orderEntity.getStatus().equals(Status.CANCELLED)){
                throw  new RuntimeException("");
            }
            // Update status
            orderEntity.setStatus(newStatus);

            OrderEntity updatedOrder = orderRepository.save(orderEntity);

            log.info("Order status updated successfully to [{}] for ID [{}]", newStatus, id);
            return new UpdateOrderStatusResponseDTO("Status are updated successfully");

        } catch (OrderNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update order status for ID [{}]", id, e);
            throw new RuntimeException("Failed to update order status", e);
        }
    }

    public List<GetUserHistoryByStatusAndDateResponseDTO> getUserHistoryByOptionalFilters(
            Status status, LocalDate startDate, LocalDate endDate) {

        Long userId = UserContextHolder.getUser().getId();
        List<RoleEnum> roles = UserContextHolder.getUser().getRoles();

        log.info("[ORDER FILTER] Request by userId={}, Roles={}, Status={}, StartDate={}, EndDate={}",
                userId, roles, status, startDate, endDate);

        if (roles == null || roles.isEmpty()) {
            log.warn("[ORDER FILTER] User has no roles assigned.");
            throw new UnauthorizedAccessException("User has no roles assigned.");
        }

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(23, 59, 59) : null;

        List<OrderEntity> orders;

        if (roles.contains(RoleEnum.CUSTOMER)) {
            log.info("[ORDER FILTER] Fetching orders for CUSTOMER with userId={}", userId);
            orders = orderRepository.findCustomerOrdersWithOptionalFilters(userId, status, start, end);
        } else if (roles.contains(RoleEnum.STOREKEEPER)) {
            log.info("[ORDER FILTER] Fetching orders for STOREKEEPER with userId={}", userId);
            orders = orderRepository.findStorekeeperOrdersWithOptionalFilters(userId, status, start, end);
        } else {
            log.error("[ORDER FILTER] Unauthorized role access for userId={}", userId);
            throw new UnauthorizedAccessException("User role not authorized to access order history.");
        }

        if (orders.isEmpty()) {
            log.warn("[ORDER FILTER] No orders found for userId={} with filters: Status={}, Start={}, End={}",
                    userId, status, start, end);
            throw new OrderNotFoundException("No orders found with given filters.");
        }

        log.info("[ORDER FILTER] {} orders found for userId={}", orders.size(), userId);

        return orders.stream()
                .map(GetUserHistoryByStatusAndDateResponseDTO::fromEntity)
                .toList();
    }

    public List<GetOrderByStoreKeeperResponseDTO> getOrdersByStorekeeper() {

        Long storekeeperId = UserContextHolder.getUser().getId();
        log.info("[STOREKEEPER ORDERS] Fetching orders for storeKeeperId={}", storekeeperId);

        List<OrderEntity> orders = orderRepository.findByStoreKeeperId(storekeeperId);

        if (orders.isEmpty()) {
            log.warn("[STOREKEEPER ORDERS] No orders found for storeKeeperId={}", storekeeperId);
            throw new OrderNotFoundException("No orders found for StoreKeeper ID: " + storekeeperId);
        }

        List<GetOrderByStoreKeeperResponseDTO> responseDTOs = orders.stream()
                .map(GetOrderByStoreKeeperResponseDTO::toEntity)
                .toList();

        log.info("[STOREKEEPER ORDERS] {} order(s) found for storeKeeperId={}", responseDTOs.size(), storekeeperId);
        return responseDTOs;
    }

    public RepeatOrderResponseDTO repeatOrder(Long id)
    {
        log.info("Repeat Order with Id = {}",id);
        OrderEntity existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));

        Long customerId = UserContextHolder.getUser().getId();
        if (!existingOrder.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("You are not authorized to repeat this order.");
        }
        // create new order
        OrderEntity newOrder = new OrderEntity();
        newOrder.setCustomer(existingOrder.getCustomer());
        newOrder.setDeliveryAddress(existingOrder.getDeliveryAddress());
        newOrder.setStoreKeeper(existingOrder.getStoreKeeper());
        newOrder.setStatus(Status.PENDING);


        List<OrderItemEntity> newItems = new ArrayList<>();
        for (OrderItemEntity item : existingOrder.getOrderItems()) {
            OrderItemEntity clonedItem = new OrderItemEntity();
            clonedItem.setOrders(newOrder);
            clonedItem.setItem(item.getItem());
            clonedItem.setItemName(item.getItemName());
            clonedItem.setQuantity(item.getQuantity());
            clonedItem.setUnit(item.getUnit());
            clonedItem.setPrice(item.getPrice());

            newItems.add(clonedItem);
        }
        newOrder.setOrderItems(newItems);
        orderRepository.save(newOrder);

        log.info("New order repeated with ID={}", newOrder.getId());
        return new RepeatOrderResponseDTO("Order repeated successfully. New Order ID: " + newOrder.getId());

    }

    public DispatchOrderResponseDTO dispatchOrder(DispatchOrderRequestDTO request) {
        Long storekeeperId = UserContextHolder.getUser().getId();
        log.info("[DISPATCH] Initiating dispatch for orderId={} by storekeeperId={}", request.getOrderId(), storekeeperId);

        // Validate item list
        if (request.getOrderItem() == null || request.getOrderItem().isEmpty()) {
            log.warn("[DISPATCH] Item list is empty or null for orderId={}", request.getOrderId());
            throw new IllegalArgumentException("Item list must not be null or empty.");
        }

        // Fetch the order
        OrderEntity order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> {
                    log.warn("[DISPATCH] Order not found with ID={}", request.getOrderId());
                    return new OrderNotFoundException("Order not found with ID: " + request.getOrderId());
                });

        // Validate ownership
        if (!order.getStoreKeeper().getId().equals(storekeeperId)) {
            log.error("[DISPATCH] Unauthorized attempt by storekeeperId={} for orderId={}", storekeeperId, order.getId());
            throw new RuntimeException("You are not authorized to dispatch this order.");
        }

        // Update each item's price
        for (OrderItemEntity orderItem : order.getOrderItems()) {
            request.getOrderItem().stream()
                    .filter(i -> i.getItemId().equals(orderItem.getItem().getId()))
                    .findFirst()
                    .ifPresent(i -> {
                        log.info("[DISPATCH] Updating price for itemId={} to {}", i.getItemId(), i.getPrice());
                        orderItem.setPrice(i.getPrice());
                    });
        }

        // Set status and optional note
        order.setStatus(Status.DISPATCH);
        order.setNote(request.getNote());

        // Save updated order
        orderRepository.save(order);

        log.info("[DISPATCH] Order ID={} dispatched successfully by storekeeperId={}", order.getId(), storekeeperId);
        return new DispatchOrderResponseDTO("Order dispatched successfully.");
    }
}
