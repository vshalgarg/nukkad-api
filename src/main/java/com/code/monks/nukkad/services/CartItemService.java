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
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.CartItemRepository;
import com.code.monks.nukkad.repositories.CartRepository;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        log.info("Initiating add-to-cart process for customerId={}", customerId);

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.warn("Customer not found in database. customerId={}", customerId);
                    return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
                });

        CartEntity cart = cartRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    log.info("No existing cart found for customerId={}. Creating a new cart.", customerId);
                    CartEntity newCart = new CartEntity();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });

        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
            log.info("Cart item list was null. Initialized empty item list for cartId={}", cart.getId());
        }

        for (CreateCartItemRequestDTO.CartItemRequest itemReq : dto.getItems()) {
            Long itemId =  itemReq.getItemId();
            log.info("Processing cart addition for itemId={} (unit={}, quantity={})",
                    itemId, itemReq.getUnit(), itemReq.getQuantity());

            ItemEntity item = itemRepository.findById(itemId)
                    .orElseThrow(() -> {
                        log.warn("Item not found in DB. itemId={}", itemId);
                        return new ResourceNotFoundException(ITEM_NOT_FOUND, itemId);
                    });

            String requestedUnit = itemReq.getUnit();
            String[] allowedUnits = item.getUnit().getUnits();

            boolean isValidUnit = Arrays.stream(allowedUnits)
                    .anyMatch(unit -> unit.equalsIgnoreCase(requestedUnit));
            if (!isValidUnit) {
                log.warn("Invalid unit '{}' for itemId={}. Allowed units: {}", requestedUnit, itemId, Arrays.toString(allowedUnits));
                throw new IllegalArgumentException("Invalid unit: '" + requestedUnit + "'. Allowed: " + String.join(", ", allowedUnits));
            }

            Optional<CartItemEntity> existingItem = cart.getItems().stream()
                    .filter(ci -> ci.getItem().getId().equals(itemId) &&
                            ci.getUnit().equalsIgnoreCase(requestedUnit))
                    .findFirst();

            if (existingItem.isPresent()) {
                CartItemEntity cartItem = existingItem.get();
                int oldQty = cartItem.getQuantity();
                cartItem.setQuantity(oldQty + itemReq.getQuantity());
                log.info("Updated existing cart item. itemId={}, oldQty={}, newQty={}",
                        itemId, oldQty, cartItem.getQuantity());
            } else {
                CartItemEntity cartItem = new CartItemEntity();
                cartItem.setCart(cart);
                cartItem.setItem(item);
                cartItem.setCustomer(customer);
                cartItem.setQuantity(itemReq.getQuantity());
                cartItem.setUnit(requestedUnit.toUpperCase());
                cart.getItems().add(cartItem);
                log.info("Added new item to cart. itemId={}, quantity={}, unit={}",
                        itemId, itemReq.getQuantity(), requestedUnit.toUpperCase());
            }
        }

        cartRepository.save(cart);
        log.info("Cart updated and saved successfully for customerId={}", customerId);
        return new CreateCartItemResponseDTO("Items added to cart successfully.");
    }





    public List<GetCartItemResponseDto> getCartItemsForCustomer() {
        try {
            Long customerId = UserContextHolder.getRequiredUser().getId();
            log.info("Fetching cart items for customerId: {}", customerId);


            if (!customerRepository.existsById(customerId)) {
                log.warn("Customer with ID {} not found", customerId);
                throw new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
            }

            List<CartItemEntity> cartItems = cartItemRepository.findByCustomerId(customerId);

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

            item.setQuantity(newQty);
            CartItemEntity updated = cartItemRepository.save(item);

            log.info("Updated quantity for cart item. CartItemId: {}, NewQuantity: {}, CustomerId: {}", cartItemId, newQty,userId);
            return new UpdateItemQuantityResponseDto("success");
        }
    }

