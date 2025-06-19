package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateAddressRequestDTO;
import com.code.monks.nukkad.dto.request.CreateOrderRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateOrderRequestDTO;
import com.code.monks.nukkad.dto.response.OrderResponseDTO;
import com.code.monks.nukkad.dto.response.StatusResponseDTO;
import com.code.monks.nukkad.entities.*;
import com.code.monks.nukkad.enums.OrderStatusEnum;
import com.code.monks.nukkad.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final StorekeeperRepository storekeeperRepository;
    private final AddressRepository addressRepository;

    public OrderResponseDTO createOrder(CreateOrderRequestDTO dto){
        Long customerId = UserContextHolder.getRequiredUser().getId();
        Long storekeeperId = 1L;

        // Get CartItemEntity from cartItemId
        CartItemEntity cartItem = cartItemRepository.findById(dto.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        OrderEntity order = new OrderEntity();
        order.setCustomerId(customerId);
        order.setStorekeeperId(storekeeperId);
        order.setCartItem(cartItem);
        order.setStatus(OrderStatusEnum.PENDING);

        order = orderRepository.save(order);
        return enrichOrderResponse(OrderResponseDTO.fromEntity(order), order);
    }

    public OrderResponseDTO getOrderById(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return enrichOrderResponse(OrderResponseDTO.fromEntity(order), order);
    }
    public StatusResponseDTO updateOrder(Long id, UpdateOrderRequestDTO dto) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStorekeeperId(dto.getStorekeeperId());
        order.setStatus(dto.getStatus());
        orderRepository.save(order);
        return new StatusResponseDTO("Status has been changed to \"" + dto.getStatus().name() + "\"");
    }

    private OrderResponseDTO enrichOrderResponse(OrderResponseDTO dto, OrderEntity order) {
        // Customer info
        CustomerEntity customer = customerRepository.findById(order.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        dto.setCustomerName(customer.getName());

        // Fetch default address from address repository
        AddressEntity address = addressRepository.findByUserIdAndIsDefaultTrue(customer.getId())
                .orElseThrow(() -> new RuntimeException("Default address not found"));

        StringBuilder customerAddress = new StringBuilder(address.getAddressLine1());
        if (address.getAddressLine2() != null && !address.getAddressLine2().isBlank()) {
            customerAddress.append(", ").append(address.getAddressLine2());
        }
        dto.setCustomerAddress(customerAddress.toString());

        // Storekeeper info
        StorekeeperEntity storekeeper = storekeeperRepository.findById(order.getStorekeeperId())
                .orElseThrow(() -> new RuntimeException("Storekeeper not found"));

        dto.setStoreName(storekeeper.getStoreName());

        return dto;
    }

}
