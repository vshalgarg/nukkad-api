
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
    private final UserNotificationService userNotificationService;
    private final UserDeviceTokenService userDeviceTokenService;

    public PlaceOrderResponseDTO placeOrders(PlaceOrderRequestDTO requestDTO) {
        log.info("[ORDER] Place‑order request: {}", requestDTO);
        Long customerId = UserContextHolder.getUser().getId();

        try {
            CustomerEntity customer = validateCustomer(customerId);
            StorekeeperEntity storekeeper = validateStorekeeper(requestDTO.getStoreKeeperId(), customer);
            AddressEntity deliveryAddress = validateAddress(requestDTO.getDeliveryAddressId(), customerId);

            OrderEntity order = prepareOrder(customer, storekeeper, deliveryAddress);
            List<CartItemEntity> cartItems = getCartItems(customerId);
            List<OrderItemEntity> orderItems = convertToOrderItems(cartItems, order);
            order.setOrderItems(orderItems);

            saveOrderAndClearCart(order, cartItems, customerId);
            notifyUsers(customerId, storekeeper.getId(), order.getId());

            return new PlaceOrderResponseDTO("Order placed successfully");

        } catch (ResourceNotFoundException | InvalidRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("[ORDER] Unexpected failure while placing order (customerId={})", customerId, ex);
            throw new UnhandledException(UNHANDLED_EXCEPTION, ex);
        }
    }
    private CustomerEntity validateCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.warn("[ORDER] Customer not found (id={})", customerId);
                    return new ResourceNotFoundException(ResponseErrorCodes.CUSTOMER_NOT_FOUND);
                });
    }

    private StorekeeperEntity validateStorekeeper(Long storekeeperId, CustomerEntity customer) {
        StorekeeperEntity storekeeper = storekeeperRepository.findById(storekeeperId)
                .orElseThrow(() -> {
                    log.warn("[ORDER] Storekeeper not found (id={})", storekeeperId);
                    return new ResourceNotFoundException(ResponseErrorCodes.STOREKEEPER_NOT_FOUND, storekeeperId);
                });

        if (customer.getStorekeepers() == null ||
                customer.getStorekeepers().stream().noneMatch(sk -> sk.getId().equals(storekeeperId))) {
            log.warn("[ORDER] Storekeeper mismatch: customerId={}, storekeeperId={}", customer.getId(), storekeeperId);
            throw new InvalidRequestException(ResponseErrorCodes.STOREKEEPER_CUSTOMER_MISMATCH);
        }

        return storekeeper;
    }

    private AddressEntity validateAddress(Long addressId, Long customerId) {
        AddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    log.warn("[ORDER] Delivery address not found (id={})", addressId);
                    return new ResourceNotFoundException(ResponseErrorCodes.ADDRESS_NOT_FOUND);
                });

        if (!address.getCustomerId().equals(customerId)) {
            log.warn("[ORDER] Address mismatch: customerId={}, addressId={}", customerId, addressId);
            throw new InvalidRequestException(ResponseErrorCodes.ADDRESS_CUSTOMER_MISMATCH);
        }

        return address;
    }

    private OrderEntity prepareOrder(CustomerEntity customer, StorekeeperEntity storekeeper, AddressEntity address) {
        AddressSnapshotDTO snapshotDTO = new AddressSnapshotDTO(
                address.getName(),
                address.getMobileNumber(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getLandmark(),
                address.getCity(),
                address.getState(),
                address.getPincode()
        );

        return OrderEntity.builder()
                .customer(customer)
                .storeKeeper(storekeeper)
                .deliveryAddress(address)  // entity relationship as before
                .deliveryAddressSnapshot(snapshotDTO) // set DTO instead of String
                .status(OrderStatusEnum.PENDING)
                .build();
    }


    private List<CartItemEntity> getCartItems(Long customerId) {
        List<CartItemEntity> cartItems = Optional
                .ofNullable(cartItemRepository.findByCustomerId(customerId))
                .orElseGet(ArrayList::new);

        if (cartItems.isEmpty()) {
            log.warn("[ORDER] Cart empty — aborting (customerId={})", customerId);
            throw new ResourceNotFoundException(ResponseErrorCodes.CART_EMPTY);
        }

        log.debug("[ORDER] {} cart item(s) fetched for customerId={}", cartItems.size(), customerId);
        return cartItems;
    }

    private List<OrderItemEntity> convertToOrderItems(List<CartItemEntity> cartItems, OrderEntity order) {
        return cartItems.stream()
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
    }

    private void saveOrderAndClearCart(OrderEntity order, List<CartItemEntity> cartItems, Long customerId) {
        orderRepository.save(order);
        log.info("[ORDER] Order saved (orderId={}, customerId={})", order.getId(), customerId);
        cartItemRepository.deleteAll(cartItems);
        log.debug("[ORDER] Cart cleared ({} item[s]) for customerId={}", cartItems.size(), customerId);
    }

    private void notifyUser(Long userId, RoleEnum role, String title, String message) {
        userDeviceTokenService.getDeviceTokenForUser(userId, role).ifPresentOrElse(
                token -> {
                    notificationService.sendNotification(token, title, message);
                    userNotificationService.saveNotification(userId, title, message);
                    log.info("[NOTIFY] Sent to {} (id={})", role.name(), userId);
                },
                () -> log.warn("No device token found for {}Id: {}", role.name().toLowerCase(), userId)
        );
    }

    private void notifyUsers(Long customerId, Long storekeeperId, Long orderId) {
        String title = "Order #" + orderId;

        notifyUser(
                customerId,
                RoleEnum.CUSTOMER,
                title, "Your order has been placed");
        notifyUser(
                storekeeperId,
                RoleEnum.STOREKEEPER,
                title,
                "You have received a new order");
    }


    public UpdateOrderStatusResponseDTO updateOrderStatus(long id, UpdateOrderStatusRequestDTO request) {
        OrderStatusEnum newStatus = validateStatus(request.getOrderStatus());
        log.info("Updating status for Order ID [{}] to [{}]", id, newStatus);

        try {
            OrderEntity order = fetchOrderOrThrow(id);
            updateStatus(order, newStatus);

            sendNotifications(order, newStatus);

            return new UpdateOrderStatusResponseDTO("Status updated successfully");

        } catch (OrderNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update order status for ID [{}]", id, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }
    private OrderStatusEnum validateStatus(OrderStatusEnum status) {
        if (status == null) {
            log.error("Status in request is null");
            throw new IllegalArgumentException("Status cannot be null");
        }
        return status;
    }

    private OrderEntity fetchOrderOrThrow(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
    }

    private void updateStatus(OrderEntity order, OrderStatusEnum newStatus) {
        order.setStatus(newStatus);
        orderRepository.save(order);
        log.info("Order status updated successfully to [{}] for ID [{}]", newStatus, order.getId());
    }

    private void sendNotifications(OrderEntity order, OrderStatusEnum newStatus) {
        String title = "Order #" + order.getId();
        String statusText = newStatus.name().toLowerCase().replace("_", " ");

        notifyUser(
                order.getCustomer().getId(),
                RoleEnum.CUSTOMER, title,
                "Your order has been " + statusText + " successfully"
        );
    }


    public PagedOrderHistoryResponseDTO getUserHistoryByOptionalFilters(
            OrderStatusEnum status,
            LocalDate startDate,
            LocalDate endDate,
            Double minPrice,
            Double maxPrice,
            Pageable pageable) {

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

        Page<OrderEntity> ordersPage;

        if (roles.contains(RoleEnum.CUSTOMER)) {
            log.info("[ORDER FILTER] Fetching orders for CUSTOMER with userId={}", userId);
            ordersPage = orderRepository.findCustomerOrdersWithFilters(userId, status, start, end, minPrice, maxPrice, pageable);
        } else if (roles.contains(RoleEnum.STOREKEEPER)) {
            log.info("[ORDER FILTER] Fetching orders for STOREKEEPER with userId={}", userId);
            ordersPage = orderRepository.findStorekeeperOrdersWithFilters(userId, status, start, end, minPrice, maxPrice, pageable);
        } else {
            log.error("[ORDER FILTER] Unauthorized role access for userId={}", userId);
            throw new UnauthorizedAccessException("User role not authorized to access order history.");
        }

        List<GetUserHistoryByStatusAndDateResponseDTO> orders = ordersPage.getContent()
                .stream()
                .map(GetUserHistoryByStatusAndDateResponseDTO::fromEntity)
                .toList();

        return new PagedOrderHistoryResponseDTO(
                orders,
                ordersPage.getTotalElements(),
                ordersPage.getTotalPages(),
                ordersPage.getNumber(),
                ordersPage.getSize()
        );
    }


    public GetOrdersResponseDTO getOrdersByStorekeeper(OrderStatusFilterEnum statusFilter, int page, int size) {
        Long storekeeperId = UserContextHolder.getUser().getId();
        log.info("[STOREKEEPER ORDERS] Fetching orders for storeKeeperId={} with status filter={} and page={}, size={}",
                storekeeperId, statusFilter, page, size);

        List<OrderStatusEnum> statuses = statusFilter.getStatusEnums();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<OrderEntity> pagedOrders = orderRepository.findByStoreKeeperIdAndStatuses(storekeeperId, statuses, pageable);

        List<GetOrderByStoreKeeperResponseDTO> orderDTOs = pagedOrders
                .getContent()
                .stream()
                .map(GetOrderByStoreKeeperResponseDTO::toEntity)
                .toList();

        return GetOrdersResponseDTO.builder()
                .orders(orderDTOs)
                .totalOrders(pagedOrders.getTotalElements())
                .totalPages(pagedOrders.getTotalPages())
                .currentPage(pagedOrders.getNumber())
                .pageSize(pagedOrders.getSize())
                .build();
    }


    public DispatchOrderResponseDTO dispatchOrder(DispatchOrderRequestDTO request) {
        Long storekeeperId = UserContextHolder.getUser().getId();
        Long orderId = request.getOrderId();
        log.info("[DISPATCH] Request received to dispatch orderId={} by storekeeperId={}", orderId, storekeeperId);

        try {
            validateOrderItems(request);
            OrderEntity order = fetchOrderOrThrow(orderId);
            validateStorekeeperAccess(order, storekeeperId);
            updateItemPrices(order, request);
            updateOrderStatusAndNote(order, request);
            notifyCustomerAndStorekeeper(order, storekeeperId);

            return new DispatchOrderResponseDTO("Order dispatched successfully.");
        } catch (IllegalArgumentException | OrderNotFoundException | UnauthorizedAccessException e) {
            log.warn("[DISPATCH] Error while processing orderId={}: {}", orderId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[DISPATCH] Unexpected error while dispatching orderId={}", orderId, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }
    private void validateOrderItems(DispatchOrderRequestDTO request) {
        if (request.getOrderItem() == null || request.getOrderItem().isEmpty()) {
            throw new IllegalArgumentException("Order items list cannot be empty or null.");
        }
    }
    private OrderEntity fetchOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("[DISPATCH] Order not found with ID={}", orderId);
                    return new OrderNotFoundException(ORDER_NOT_FOUND);
                });
    }
    private void validateStorekeeperAccess(OrderEntity order, Long storekeeperId) {
        if (!order.getStoreKeeper().getId().equals(storekeeperId)) {
            log.error("[DISPATCH] Unauthorized dispatch attempt by storekeeperId={} for orderId={}", storekeeperId, order.getId());
            throw new UnauthorizedAccessException("Unauthorized to dispatch this order.");
        }
    }
    private void updateItemPrices(OrderEntity order, DispatchOrderRequestDTO request) {
        request.getOrderItem().forEach(requestedItem -> {
            order.getOrderItems().stream()
                    .filter(orderItem -> orderItem.getItem().getId().equals(requestedItem.getItemId()))
                    .findFirst()
                    .ifPresent(orderItem -> {
                        log.debug("[DISPATCH] Updating price for itemId={} to {}", requestedItem.getItemId(), requestedItem.getPrice());
                        orderItem.setPrice(requestedItem.getPrice());
                    });
        });
    }
    private void updateOrderStatusAndNote(OrderEntity order, DispatchOrderRequestDTO request) {
        order.setStatus(OrderStatusEnum.DISPATCHED);
        order.setStoreKeeperNote(request.getStoreKeeperNote());
        orderRepository.save(order);
        log.info("[DISPATCH] Order ID={} dispatched successfully", order.getId());
    }

    private void notifyCustomerAndStorekeeper(OrderEntity order, Long storekeeperId) {
        String title = "Order #" + order.getId();

        notifyUser(
                order.getCustomer().getId(),
                RoleEnum.CUSTOMER,
                title,
                "Your order has been dispatched successfully."
        );
    }
}
