
package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.DispatchOrderRequestDTO;
import com.code.monks.nukkad.dto.request.PlaceOrderRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateOrderStatusRequestDTO;
import com.code.monks.nukkad.dto.response.*;
import com.code.monks.nukkad.entities.*;
import com.code.monks.nukkad.enums.OrderStatusFilterEnum;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.enums.OrderStatusEnum;
import com.code.monks.nukkad.exception.*;
import com.code.monks.nukkad.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.ORDER_NOT_FOUND;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNHANDLED_EXCEPTION;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final CartItemRepository cartItemRepository;
    private final StorekeeperRepository storekeeperRepository;
    private final NotificationService notificationService;
    private final UserDeviceTokenRepository userDeviceTokenRepository;

    public PlaceOrderResponseDTO placeOrders(PlaceOrderRequestDTO requestDTO) {

        log.info("[ORDER] Place‑order request: {}", requestDTO);

        Long customerId = UserContextHolder.getUser().getId();

        try {
            // Step 1: Validate Customer
            CustomerEntity customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> {
                        log.warn("[ORDER] Customer not found (id={})", customerId);
                        return new ResourceNotFoundException(ResponseErrorCodes.CUSTOMER_NOT_FOUND);
                    });

            // Step 2: Validate Storekeeper
            StorekeeperEntity storekeeper = storekeeperRepository.findById(requestDTO.getStoreKeeperId())
                    .orElseThrow(() -> {
                        log.warn("[ORDER] Storekeeper not found (id={})", requestDTO.getStoreKeeperId());
                        return new ResourceNotFoundException(ResponseErrorCodes.STOREKEEPER_NOT_FOUND, requestDTO.getStoreKeeperId());
                    });

            //  Validate Storekeeper belongs to Customer
            if (customer.getStorekeepers() == null ||
                    customer.getStorekeepers().stream().noneMatch(sk -> sk.getId().equals(storekeeper.getId()))) {
                log.warn("[ORDER] Storekeeper mismatch: customerId={}, storekeeperId={}", customerId, storekeeper.getId());
                throw new InvalidRequestException(ResponseErrorCodes.STOREKEEPER_CUSTOMER_MISMATCH);
            }

            // Step 3: Validate Delivery Address
            AddressEntity deliveryAddress = addressRepository.findById(requestDTO.getDeliveryAddressId())
                    .orElseThrow(() -> {
                        log.warn("[ORDER] Delivery address not found (id={})", requestDTO.getDeliveryAddressId());
                        return new ResourceNotFoundException(ResponseErrorCodes.ADDRESS_NOT_FOUND);
                    });

            //  Validate Address belongs to Customer
            if (!deliveryAddress.getCustomerId().equals(customerId)) {
                log.warn("[ORDER] Address mismatch: customerId={}, addressId={}", customerId, deliveryAddress.getId());
                throw new InvalidRequestException(ResponseErrorCodes.ADDRESS_CUSTOMER_MISMATCH);
            }

            // Step 4: Prepare new Order
            OrderEntity order = OrderEntity.builder()
                    .customer(customer)
                    .storeKeeper(storekeeper)
                    .deliveryAddress(deliveryAddress)
                    .status(OrderStatusEnum.PENDING)
                    .build();

            // Step 5: Fetch Cart Items
            List<CartItemEntity> cartItems = Optional
                    .ofNullable(cartItemRepository.findByCustomerId(customerId))
                    .orElseGet(ArrayList::new);

            if (cartItems.isEmpty()) {
                log.warn("[ORDER] Cart empty — aborting (customerId={})", customerId);
                throw new ResourceNotFoundException(ResponseErrorCodes.CART_EMPTY);
            }

            log.debug("[ORDER] {} cart item(s) fetched for customerId={}", cartItems.size(), customerId);

            // Step 6: Convert to Order Items
            List<OrderItemEntity> orderItems = cartItems.stream()
                    .map(ci -> {
                        OrderItemEntity oi = new OrderItemEntity();
                        oi.setItem(ci.getItem());
                        oi.setItemName(ci.getItem().getName());
                        oi.setQuantity(ci.getQuantity());
                        oi.setUnit(ci.getUnit());
                        oi.setOrders(order);
                        log.debug("[ORDER]   → itemId={}, qty={}, unit={}", ci.getItem().getId(), ci.getQuantity(), ci.getUnit());
                        return oi;
                    })
                    .toList();

            order.setOrderItems(orderItems);

            // Step 7: Persist order and cleanup cart
            orderRepository.save(order);
            log.info("[ORDER] Order saved (orderId={}, customerId={})", order.getId(), customerId);

            cartItemRepository.deleteAll(cartItems);
            log.debug("[ORDER] Cart cleared ({} item[s]) for customerId={}", cartItems.size(), customerId);

            log.info("Sending notification ");
            Optional<UserDeviceTokenEntity> tokenOpt = userDeviceTokenRepository.findByCustomerId(customerId);

            if (tokenOpt.isPresent()) {
                String deviceToken = tokenOpt.get().getDeviceToken();

                String title = "Order #"+order.getId();
                String body = "Your order has been placed";
                notificationService.sendNotification(deviceToken, title, body);
            } else {
                log.warn("No device token found for customerId: {}", customerId);
            }
            // Step 8: Return response
            return new PlaceOrderResponseDTO("Order placed successfully");

        } catch (ResourceNotFoundException | InvalidRequestException ex) {
            throw ex;

        } catch (Exception ex) {
            log.error("[ORDER] Unexpected failure while placing order (customerId={})", customerId, ex);
            throw new UnhandledException(UNHANDLED_EXCEPTION, ex);
        }
    }


    public UpdateOrderStatusResponseDTO updateOrderStatus(long id, UpdateOrderStatusRequestDTO updateOrderStatusRequestDTO) {
        OrderStatusEnum newOrderStatus = updateOrderStatusRequestDTO.getOrderStatus();
        log.info("Updating status for Order ID [{}] to [{}]", id, newOrderStatus);

        try {
            // Validate input
            if (newOrderStatus == null) {
                log.error("Status in request is null");
                throw new IllegalArgumentException("Status cannot be null");
            }

            // Fetch existing order
            OrderEntity orderEntity = orderRepository.findById(id)
                    .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));

            // Update status
            orderEntity.setStatus(newOrderStatus);

            OrderEntity updatedOrder = orderRepository.save(orderEntity);

            log.info("Order status updated successfully to [{}] for ID [{}]", newOrderStatus, id);

            // Send notification to customer
            Long customerId = updatedOrder.getCustomer().getId();
            log.info("Sending notification to customerId={}", customerId);

            Optional<UserDeviceTokenEntity> tokenOpt = userDeviceTokenRepository.findByCustomerId(customerId);

            if (tokenOpt.isPresent()) {
                String deviceToken = tokenOpt.get().getDeviceToken();

                String title = "Order #" + updatedOrder.getId();
                String body = "Your order has been " + newOrderStatus.name().toLowerCase().replace("_", " ") + " successfully";
                notificationService.sendNotification(deviceToken, title, body);
            } else {
                log.warn("No device token found for customerId: {}", customerId);
            }

            return new UpdateOrderStatusResponseDTO("Status are updated successfully");

        } catch (OrderNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update order status for ID [{}]", id, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }


    public List<GetUserHistoryByStatusAndDateResponseDTO> getUserHistoryByOptionalFilters(
            OrderStatusEnum status, LocalDate startDate, LocalDate endDate, Double minPrice, Double maxPrice) {

        Long userId = UserContextHolder.getUser().getId();
        List<RoleEnum> roles = UserContextHolder.getUser().getRoles();

        log.info("[ORDER FILTER] Request by userId={}, Roles={}, Status={}, StartDate={}, EndDate={}, MinPrice={}, MaxPrice={}",
                userId, roles, status, startDate, endDate, minPrice, maxPrice);

        if (roles == null || roles.isEmpty()) {
            log.warn("[ORDER FILTER] User has no roles assigned.");
            throw new UnauthorizedAccessException("User has no roles assigned.");
        }

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(23, 59, 59) : null;

        List<OrderEntity> orders;

        if (roles.contains(RoleEnum.CUSTOMER)) {
            log.info("[ORDER FILTER] Fetching orders for CUSTOMER with userId={}", userId);
            orders = orderRepository.findCustomerOrdersWithFilters(userId, status, start, end, minPrice, maxPrice);
        } else if (roles.contains(RoleEnum.STOREKEEPER)) {
            log.info("[ORDER FILTER] Fetching orders for STOREKEEPER with userId={}", userId);
            orders = orderRepository.findStorekeeperOrdersWithFilters(userId, status, start, end, minPrice, maxPrice);
        } else {
            log.error("[ORDER FILTER] Unauthorized role access for userId={}", userId);
            throw new UnauthorizedAccessException("User role not authorized to access order history.");
        }

        return orders.stream()
                .map(GetUserHistoryByStatusAndDateResponseDTO::fromEntity)
                .toList();
    }


    public Page<GetOrderByStoreKeeperResponseDTO> getOrdersByStorekeeper(OrderStatusFilterEnum statusFilter, int page, int size) {
        Long storekeeperId = UserContextHolder.getUser().getId();
        log.info("[STOREKEEPER ORDERS] Fetching orders for storeKeeperId={} with status filter={} and page={}, size={}", storekeeperId, statusFilter, page, size);

        List<OrderStatusEnum> statuses = statusFilter.getStatusEnums();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<OrderEntity> pagedOrders = orderRepository.findByStoreKeeperIdAndStatuses(storekeeperId, statuses, pageable);

        return pagedOrders.map(GetOrderByStoreKeeperResponseDTO::toEntity);
    }



    public DispatchOrderResponseDTO dispatchOrder(DispatchOrderRequestDTO request) {
        Long storekeeperId = UserContextHolder.getUser().getId();
        Long orderId = request.getOrderId();
        log.info("[DISPATCH] Request received to dispatch orderId={} by storekeeperId={}", orderId, storekeeperId);

        try {
            // Validate item list
            if (request.getOrderItem() == null || request.getOrderItem().isEmpty()) {
                log.warn("[DISPATCHED] Empty or null order item list for orderId={}", orderId);
                throw new IllegalArgumentException("Order items list cannot be empty or null.");
            }

            // Fetch order
            OrderEntity order = orderRepository.findById(orderId)
                    .orElseThrow(() -> {
                        log.warn("[DISPATCHED] Order not found with ID={}", orderId);
                        return new OrderNotFoundException(ORDER_NOT_FOUND );
                    });

            // Check storekeeper access
            if (!order.getStoreKeeper().getId().equals(storekeeperId)) {
                log.error("[DISPATCHED] Unauthorized dispatch attempt by storekeeperId={} for orderId={}", storekeeperId, orderId);
                throw new UnauthorizedAccessException("Unauthorized to dispatch this order.");
            }

            // Update item prices
            request.getOrderItem().forEach(requestedItem -> {
                order.getOrderItems().stream()
                        .filter(orderItem -> orderItem.getItem().getId().equals(requestedItem.getItemId()))
                        .findFirst()
                        .ifPresent(orderItem -> {
                            log.debug("[DISPATCHED] Updating price for itemId={} to {}", requestedItem.getItemId(), requestedItem.getPrice());
                            orderItem.setPrice(requestedItem.getPrice());
                        });
            });

            // Update status & note
            order.setStatus(OrderStatusEnum.DISPATCHED);
            order.setStoreKeeperNote(request.getStoreKeeperNote());

            orderRepository.save(order);
            log.info("[DISPATCHED] Order ID={} dispatched successfully by storekeeperId={}", orderId, storekeeperId);

            // Send  notification to customer
            Long customerId = order.getCustomer().getId();
            log.info("[DISPATCHED] Sending dispatch notification to customerId={}", customerId);

            Optional<UserDeviceTokenEntity> tokenOpt = userDeviceTokenRepository.findByCustomerId(customerId);

            if(tokenOpt.isPresent()){
                String deviceToken = tokenOpt.get().getDeviceToken();
                String title = "Order #" + order.getId();
                String body = "Your order has been dispatched successfully. ";
                notificationService.sendNotification(deviceToken,title,body);
            }else{
                log.warn("[DISPATCHED] No device token found for customerId={}", customerId);
            }

            return new DispatchOrderResponseDTO("Order dispatched successfully.");

        } catch (IllegalArgumentException e) {
            log.warn("[DISPATCHED] Invalid input for orderId={}: {}", orderId, e.getMessage());
            throw e;

        } catch (OrderNotFoundException e) {
            log.warn("[DISPATCHED] Order not found: {}", e.getMessage());
            throw e;

        } catch (UnauthorizedAccessException e) {
            log.error("[DISPATCHED] Authorization or validation failure for orderId={}", orderId, e);
            throw e;

        } catch (Exception e) {
            log.error("[DISPATCHED] Unexpected error while dispatching orderId={}", orderId, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }
}
