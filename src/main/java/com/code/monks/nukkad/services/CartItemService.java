package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateCartItemRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateCartItemRequestDTO;
import com.code.monks.nukkad.dto.response.*;
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
        log.info("[ADD TO CART] Initiating add-to-cart process for customerId={}", customerId);

        try {
            // Get customer
            CustomerEntity customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> {
                        log.warn("[ADD TO CART] Customer not found. customerId={}", customerId);
                        return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
                    });

            // Fetch or create cart
            CartEntity cart = cartRepository.findByCustomer(customer)
                    .orElseGet(() -> {
                        log.info("[ADD TO CART] No existing cart found. Creating new cart for customerId={}", customerId);
                        CartEntity newCart = new CartEntity();
                        newCart.setCustomer(customer);
                        return cartRepository.save(newCart);
                    });

            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
                log.info("[ADD TO CART] Cart item list initialized for cartId={}", cart.getId());
            }

            List<Long> itemIds = new ArrayList<>();

            for (CreateCartItemRequestDTO.CartItemRequest itemReq : dto.getItems()) {
                Long itemId = itemReq.getItemId();
                log.info("[ADD TO CART] Processing itemId={} (unit={}, quantity={})", itemId, itemReq.getUnit(), itemReq.getQuantity());

                ItemEntity item = itemRepository.findById(itemId)
                        .orElseThrow(() -> {
                            log.warn("[ADD TO CART] Item not found. itemId={}", itemId);
                            return new ResourceNotFoundException(ITEM_NOT_FOUND, itemId);
                        });

                String requestedUnit = itemReq.getUnit();
                String[] allowedUnits = item.getUnit().getUnits();

                boolean isValidUnit = Arrays.stream(allowedUnits)
                        .anyMatch(unit -> unit.equalsIgnoreCase(requestedUnit));
                if (!isValidUnit) {
                    log.warn("[ADD TO CART] Invalid unit '{}' for itemId={}. Allowed: {}", requestedUnit, itemId, Arrays.toString(allowedUnits));
                    throw new IllegalArgumentException("Invalid unit: '" + requestedUnit + "'. Allowed: " + String.join(", ", allowedUnits));
                }

                Optional<CartItemEntity> existingItem = cart.getItems().stream()
                        .filter(ci -> ci.getItem().getId().equals(itemId) &&
                                ci.getUnit().equalsIgnoreCase(requestedUnit))
                        .findFirst();

                if (existingItem.isPresent()) {
                    CartItemEntity cartItem = existingItem.get();
                    cartItem.setQuantity(cartItem.getQuantity() + itemReq.getQuantity());
                    itemIds.add(itemId);
                    log.info("[ADD TO CART] Updated existing cart item. itemId={}, newQty={}, cartItemId={}",
                            itemId, cartItem.getQuantity(), cartItem.getId());
                } else {
                    CartItemEntity cartItem = new CartItemEntity();
                    cartItem.setCart(cart);
                    cartItem.setItem(item);
                    cartItem.setCustomer(customer);
                    cartItem.setQuantity(itemReq.getQuantity());
                    cartItem.setUnit(requestedUnit.toUpperCase());

                    CartItemEntity savedItem = cartItemRepository.save(cartItem);
                    cart.getItems().add(savedItem);
                    itemIds.add(itemId);
                    log.info("[ADD TO CART] Added new item. itemId={}, quantity={}, unit={}, cartItemId={}",
                            itemId, itemReq.getQuantity(), requestedUnit.toUpperCase(), savedItem.getId());
                }
            }

            log.info("[ADD TO CART] Cart updated successfully for customerId={}", customerId);
            return new CreateCartItemResponseDTO("Items added to cart successfully.", itemIds);

        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("[ADD TO CART] Unexpected error occurred for customerId={}", customerId, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }




    public List<GetCartItemResponseDTO> getCartItemsForCustomer() {
        try {
            Long customerId = UserContextHolder.getRequiredUser().getId();
            log.info("Fetching cart items for customerId: {}", customerId);


            if (!customerRepository.existsById(customerId)) {
                log.warn("Customer with ID {} not found", customerId);
                throw new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
            }

            List<CartItemEntity> cartItems = cartItemRepository.findByCustomerId(customerId);

            return cartItems.stream()
                    .map(GetCartItemResponseDTO::fromEntity)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching cart items", e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }


    public RemoveCartItemResponseDTO removeCartItem(Long itemId) {
        Long customerId = UserContextHolder.getRequiredUser().getId();
        log.info("[REMOVE CART ITEM] Initiating removal for customerId={}, itemId={}", customerId, itemId);

        try {
            // Fetch the cart item by customerId and itemId
            CartItemEntity item = cartItemRepository.findByCustomerIdAndItemId(customerId, itemId)
                    .orElseThrow(() -> {
                        log.warn("[REMOVE CART ITEM] Cart item not found. itemId={}, customerId={}", itemId, customerId);
                        return new ResourceNotFoundException(ITEM_NOT_FOUND, itemId);
                    });

            cartItemRepository.delete(item);
            log.info("[REMOVE CART ITEM] Item removed successfully. itemId={}, customerId={}", itemId, customerId);

            return new RemoveCartItemResponseDTO("Cart item deleted successfully");

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("[REMOVE CART ITEM] Unexpected error while removing itemId={} for customerId={}", itemId, customerId, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }


    public UpdateCartItemResponseDTO updateCartItem(UpdateCartItemRequestDTO dto) {
        Long customerId = UserContextHolder.getRequiredUser().getId();
        Long itemId = dto.getItemId();
        String newUnit = dto.getUnit();
        Integer newQty = dto.getQuantity();

        log.info("[UPDATE CART ITEM] customerId={}, itemId={}, newUnit={}, newQty={}", customerId, itemId, newUnit, newQty);

        try {
            // Fetch existing cart item
            CartItemEntity existing = cartItemRepository.findByCustomerIdAndItemId(customerId, itemId)
                    .orElseThrow(() -> {
                        log.warn("[UPDATE CART ITEM] Cart item not found. itemId={}, customerId={}", itemId, customerId);
                        return new ResourceNotFoundException(ITEM_NOT_FOUND, itemId);
                    });

            // Validate and update unit
            if (newUnit != null && !newUnit.equalsIgnoreCase(existing.getUnit())) {
                String[] allowedUnits = existing.getItem().getUnit().getUnits();

                boolean isValidUnit = Arrays.stream(allowedUnits)
                        .anyMatch(unit -> unit.equalsIgnoreCase(newUnit));

                if (!isValidUnit) {
                    log.warn("[UPDATE CART ITEM] Invalid unit '{}' for itemId={}. Allowed units: {}", newUnit, itemId, Arrays.toString(allowedUnits));
                    throw new IllegalArgumentException("Invalid unit: '" + newUnit + "'. Allowed: " + String.join(", ", allowedUnits));
                }

                existing.setUnit(newUnit.toUpperCase());
                log.info("[UPDATE CART ITEM] Unit updated to '{}' for itemId={}", newUnit.toUpperCase(), itemId);
            }

            // Update quantity
            if (newQty != null) {
                existing.setQuantity(newQty);
                log.info("[UPDATE CART ITEM] Quantity updated to {} for itemId={}", newQty, itemId);
            }

            cartItemRepository.save(existing);
            log.info("[UPDATE CART ITEM] Cart item successfully updated. itemId={}, customerId={}", itemId, customerId);

            return new UpdateCartItemResponseDTO("Cart item updated successfully.");

        } catch (ResourceNotFoundException e) {
            throw e; // Already logged
        } catch (IllegalArgumentException e) {
            log.error("[UPDATE CART ITEM] Validation error for itemId={}, customerId={}: {}", itemId, customerId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[UPDATE CART ITEM] Unexpected error during cart item update. itemId={}, customerId={}", itemId, customerId, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }


    public ClearCartResponseDTO clearCartForCustomer() {
        Long customerId = UserContextHolder.getRequiredUser().getId();
        log.info("[CART CLEAR] Request received to clear cart for customerId={}", customerId);

        List<CartItemEntity> cartItems = cartItemRepository.findByCustomerId(customerId);

        cartItemRepository.deleteAll(cartItems);

        log.info("[CART CLEAR] Cart cleared successfully for customerId={}", customerId);
        return new ClearCartResponseDTO("Cart cleared successfully.", List.of());
    }

}

