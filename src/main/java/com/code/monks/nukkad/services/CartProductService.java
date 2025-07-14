package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateCartProductRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateCartProductRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCartProductResponseDTO;
import com.code.monks.nukkad.dto.response.GetCartProductResponseDTO;
import com.code.monks.nukkad.dto.response.UpdateCartProductResponseDto;
import com.code.monks.nukkad.dto.response.removeCartProductResponseDTO;
import com.code.monks.nukkad.entities.CartEntity;
import com.code.monks.nukkad.entities.CartProductEntity;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.CartProductRepository;
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
public class CartProductService {
    private final CartProductRepository cartProductRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;

    public CreateCartProductResponseDTO addToCart(CreateCartProductRequestDTO dto) {
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

        List<Long> itemIds = new ArrayList<>();

        for (CreateCartProductRequestDTO.CartItemRequest itemReq : dto.getItems()) {
            Long itemId = itemReq.getItemId();
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

            Optional<CartProductEntity> existingItem = cart.getItems().stream()
                    .filter(ci -> ci.getItem().getId().equals(itemId) &&
                            ci.getUnit().equalsIgnoreCase(requestedUnit))
                    .findFirst();

            if (existingItem.isPresent()) {
                CartProductEntity cartItem = existingItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + itemReq.getQuantity());
                itemIds.add(itemId);
                log.info("Updated existing cart item. itemId={}, newQty={}, cartItemId={}",
                        itemId, cartItem.getQuantity(), cartItem.getId());
            } else {
                CartProductEntity cartItem = new CartProductEntity();
                cartItem.setCart(cart);
                cartItem.setItem(item);
                cartItem.setCustomer(customer);
                cartItem.setQuantity(itemReq.getQuantity());
                cartItem.setUnit(requestedUnit.toUpperCase());

                CartProductEntity savedItem = cartProductRepository.save(cartItem);
                cart.getItems().add(savedItem);
                itemIds.add(itemId);
                log.info("Added new item to cart. itemId={}, quantity={}, unit={}, cartItemId={}",
                        itemId, itemReq.getQuantity(), requestedUnit.toUpperCase(), savedItem.getId());
            }
        }

        log.info("Cart updated and saved successfully for customerId={}", customerId);
        return new CreateCartProductResponseDTO("Items added to cart successfully.", itemIds);
    }



    public List<GetCartProductResponseDTO> getCartItemsForCustomer() {
        try {
            Long customerId = UserContextHolder.getRequiredUser().getId();
            log.info("Fetching cart items for customerId: {}", customerId);


            if (!customerRepository.existsById(customerId)) {
                log.warn("Customer with ID {} not found", customerId);
                throw new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
            }

            List<CartProductEntity> cartItems = cartProductRepository.findByCustomerId(customerId);

            return cartItems.stream()
                    .map(GetCartProductResponseDTO::fromEntity)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching cart items", e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }

    public removeCartProductResponseDTO removeCartItem(Long itemId) {

            Long customerId = UserContextHolder.getRequiredUser().getId();
            log.info("Attempting to remove cart item. CustomerId: {}, ItemId: {}", customerId, itemId);

            CartProductEntity item = cartProductRepository.findByCustomerIdAndItemId(customerId, itemId)
                    .orElseThrow(() -> {
                        log.error("Cart item with itemId {} not found for customerId {}", itemId, customerId);
                        return new ResourceNotFoundException(ITEM_NOT_FOUND, itemId);
                    });


        cartProductRepository.delete(item);
            log.info("Cart item with id {} deleted successfully for customerId: {}",item, customerId);

            return new removeCartProductResponseDTO("Cart item deleted successfully");

    }


    public UpdateCartProductResponseDto updateCartProduct(UpdateCartProductRequestDTO dto) {
        Long customerId = UserContextHolder.getRequiredUser().getId();
        Long itemId = dto.getItemId();
        String newUnit = dto.getUnit();
        Integer newQty = dto.getQuantity();

        log.info("Update cart: customerId={}, itemId={}, newUnit={}, newQty={}", customerId, itemId, newUnit, newQty);

        CartProductEntity existing = cartProductRepository.findByCustomerIdAndItemId(customerId, itemId)
                .orElseThrow(() -> new ResourceNotFoundException(ITEM_NOT_FOUND, itemId));

        // ✅ Step 1: Validate unit BEFORE setting
        if (newUnit != null && !newUnit.equalsIgnoreCase(existing.getUnit())) {
            String[] allowedUnits = existing.getItem().getUnit().getUnits();

            boolean isValidUnit = Arrays.stream(allowedUnits)
                    .anyMatch(unit -> unit.equalsIgnoreCase(newUnit));

            if (!isValidUnit) {
                log.warn("Invalid unit '{}' for itemId={}. Allowed units: {}", newUnit, itemId, Arrays.toString(allowedUnits));
                throw new IllegalArgumentException("Invalid unit: '" + newUnit + "'. Allowed: " + String.join(", ", allowedUnits));
            }

            // ✅ Only set if valid
            existing.setUnit(newUnit.toUpperCase());
        }

        // ✅ Step 2: Update quantity if provided
        if (newQty != null) {
            existing.setQuantity(newQty);
        }

        cartProductRepository.save(existing);

        return new UpdateCartProductResponseDto("Cart item updated successfully.");
    }


}

