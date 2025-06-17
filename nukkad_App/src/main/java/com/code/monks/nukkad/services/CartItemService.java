package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateCartItemRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCartItemResponseDTO;
import com.code.monks.nukkad.dto.response.GetCartItemResponseDto;
import com.code.monks.nukkad.dto.response.UpdateItemQuantityResponseDto;
import com.code.monks.nukkad.dto.response.removeCartItemResponseDto;
import com.code.monks.nukkad.entities.CartItemEntity;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnauthorizedAccessException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.CartItemRepository;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;

    public CreateCartItemResponseDTO addToCart(CreateCartItemRequestDTO dto) {
        Long userId = UserContextHolder.getRequiredUser().getId();

        CustomerEntity customer = customerRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Customer not found with id {}", userId);
                    return new RuntimeException("CUSTOMER_NOT_FOUND");
                });

        for (CreateCartItemRequestDTO.CartItemRequest itemReq : dto.getItems()) {
            log.info("Adding item to cart. CustomerId: {}, ItemId: {}, Quantity: {}, Unit: {}",
                    userId, itemReq.getItemId(), itemReq.getQuantity(), itemReq.getUnit());

            ItemEntity item = itemRepository.findById(itemReq.getItemId())
                    .orElseThrow(() -> {
                        log.error("Item not found with id {}", itemReq.getItemId());
                        return new RuntimeException("ITEM_NOT_FOUND");
                    });

            // Validate the unit
            String requestedUnit = itemReq.getUnit();
            String[] allowedUnits = item.getUnit().getUnits(); // from UnitEnum

            boolean isValidUnit = Arrays.stream(allowedUnits)
                    .anyMatch(unit -> unit.equalsIgnoreCase(requestedUnit));

            if (!isValidUnit) {
                log.warn("Invalid unit '{}' for item '{}'. Allowed units: {}",
                        requestedUnit, item.getName(), Arrays.toString(allowedUnits));
                throw new IllegalArgumentException("Invalid unit: '" + requestedUnit + "'. Allowed: " + String.join(", ", allowedUnits));
            }

            CartItemEntity cartItem = new CartItemEntity();
            cartItem.setCustomer(customer); // use fetched, managed customer entity
            cartItem.setItem(item);
            cartItem.setQuantity(itemReq.getQuantity());
            cartItem.setUnit(requestedUnit.toUpperCase()); // store in uppercase for consistency

            CartItemEntity saved = cartItemRepository.save(cartItem);
            log.info("Item saved in cart successfully. CartItemId: {}", saved.getId());
        }

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

    public removeCartItemResponseDto removeCartItem(Long cartItemId) {

            Long userId = UserContextHolder.getRequiredUser().getId();
            log.info("Attempting to remove cart item. CustomerId: {}, CartItemId: {}", userId, cartItemId);

            CartItemEntity item = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> {
                        log.error("Cart item not found with id: {}", cartItemId);
                        return new ResourceNotFoundException(CART_ITEM_NOT_FOUND, cartItemId);
                    });

//            if (!item.getCustomer().getId().equals(userId)) {
//                log.warn("Unauthorized attempt to delete cart item. OwnerId: {}, RequesterId: {}", item.getCustomer().getId(), userId);
//                throw new UnauthorizedAccessException("You are not authorized to delete this cart item.");
//            }

            cartItemRepository.delete(item);
            log.info("Cart item with id {} deleted successfully for customerId: {}", cartItemId, userId);

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

