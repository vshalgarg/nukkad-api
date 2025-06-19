package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateCartItemRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCartItemResponseDTO;
import com.code.monks.nukkad.dto.response.GetCartItemResponseDto;
import com.code.monks.nukkad.dto.response.UpdateItemQuantityResponseDto;
import com.code.monks.nukkad.dto.response.removeCartItemResponseDto;
import com.code.monks.nukkad.entities.CartEntity;
import com.code.monks.nukkad.entities.CartItemEntity;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnauthorizedAccessException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.CartItemRepository;
import com.code.monks.nukkad.repositories.CartRepository;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;

    public CreateCartItemResponseDTO addToCart(CreateCartItemRequestDTO dto) {
        Long customerId = UserContextHolder.getRequiredUser().getId();

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("CUSTOMER_NOT_FOUND"));

        // Find or create cart for customer
        CartEntity cart = cartRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });

        for (CreateCartItemRequestDTO.CartItemRequest itemReq : dto.getItems()) {

            ItemEntity item = itemRepository.findById(itemReq.getItemId())
                    .orElseThrow(() -> new RuntimeException("ITEM_NOT_FOUND"));

            // Validate unit
            String requestedUnit = itemReq.getUnit();
            String[] allowedUnits = item.getUnit().getUnits();
            boolean isValidUnit = Arrays.stream(allowedUnits)
                    .anyMatch(unit -> unit.equalsIgnoreCase(requestedUnit));
            if (!isValidUnit) {
                throw new IllegalArgumentException("Invalid unit: '" + requestedUnit + "'. Allowed: " + String.join(", ", allowedUnits));
            }

            // Check if item already exists in cart
            Optional<CartItemEntity> existingItem = cart.getItems().stream()
                    .filter(ci -> ci.getItem().getId() == item.getId() &&
                            ci.getUnit().equalsIgnoreCase(requestedUnit))
                    .findFirst();

            if (existingItem.isPresent()) {
                CartItemEntity cartItem = existingItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + itemReq.getQuantity());
            } else {
                CartItemEntity cartItem = new CartItemEntity();
                cartItem.setCart(cart);
                cartItem.setItem(item);
                cartItem.setCustomer(customer);
                cartItem.setQuantity(itemReq.getQuantity());
                cartItem.setUnit(requestedUnit.toUpperCase());
                cart.getItems().add(cartItem);
            }
        }

        cartRepository.save(cart); // Cascade will save items too
        return new CreateCartItemResponseDTO("Items added to cart successfully.");
    }




    public List<GetCartItemResponseDto> getCartItemsForCustomer() {
        try {
            Long userId = UserContextHolder.getRequiredUser().getId();
            log.info("Fetching cart items for customerId: {}", userId);


            if (!customerRepository.existsById(userId)) {
                log.warn("Customer with ID {} not found", userId);
                throw new ResourceNotFoundException(CUSTOMER_NOT_FOUND, userId);
            }

            List<CartItemEntity> cartItems = cartItemRepository.findByCustomerId(userId);

            return cartItems.stream()
                    .map(GetCartItemResponseDto::fromEntity)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching cart items", e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }

    public removeCartItemResponseDto removeCartItem(Long itemId) {

            Long customerId = UserContextHolder.getRequiredUser().getId();
            log.info("Attempting to remove cart item. CustomerId: {}, ItemId: {}", customerId, itemId);

            CartItemEntity item = cartItemRepository.findByCustomerIdAndItemId(customerId, itemId)
                    .orElseThrow(() -> {
                        log.error("Cart item with itemId {} not found for customerId {}", itemId, customerId);
                        return new ResourceNotFoundException(ITEM_NOT_FOUND, itemId);
                    });

//            if (!item.getCustomer().getId().equals(userId)) {
//                log.warn("Unauthorized attempt to delete cart item. OwnerId: {}, RequesterId: {}", item.getCustomer().getId(), userId);
//                throw new UnauthorizedAccessException("You are not authorized to delete this cart item.");
//            }

            cartItemRepository.delete(item);
            log.info("Cart item with id {} deleted successfully for customerId: {}",item, customerId);

            return new removeCartItemResponseDto("Cart item deleted successfully");

    }


    public UpdateItemQuantityResponseDto updateQuantity(Long cartItemId, int newQty) {

            Long userId = UserContextHolder.getRequiredUser().getId();
            log.info("Request to update quantity. CartItemId: {}, NewQuantity: {}, CustomerId: {}", cartItemId, newQty,userId);

            CartItemEntity item = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> {
                        log.error("Cart item not found with id: {}", cartItemId);
                        return new ResourceNotFoundException(CART_ITEM_NOT_FOUND,cartItemId);
                    });

//            if (!item.getCustomer().getId().equals(userId)) {
//                log.warn("Unauthorized update attempt. OwnerId: {}, RequesterId: {}", item.getCustomer().getId(),userId);
//                throw new UnauthorizedAccessException("You are not authorized to update this cart item.");
//            }

            item.setQuantity(newQty);
            CartItemEntity updated = cartItemRepository.save(item);

            log.info("Updated quantity for cart item. CartItemId: {}, NewQuantity: {}, CustomerId: {}", cartItemId, newQty,userId);
            return new UpdateItemQuantityResponseDto("success");
        }
    }

