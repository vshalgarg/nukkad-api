//package com.code.monks.nukkad.services;
//
//import com.code.monks.nukkad.context.UserContextHolder;
//import com.code.monks.nukkad.dto.request.CreateCartItemRequestDTO;
//import com.code.monks.nukkad.dto.response.CreateCartItemResponseDTO;
//import com.code.monks.nukkad.entities.CartItemEntity;
//import com.code.monks.nukkad.entities.ItemEntity;
//import com.code.monks.nukkad.exception.ResourceNotFoundException;
//import com.code.monks.nukkad.exception.UnauthorizedAccessException;
//import com.code.monks.nukkad.exception.UnhandledException;
//import com.code.monks.nukkad.repositories.CartItemRepository;
//import com.code.monks.nukkad.repositories.ItemRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNHANDLED_EXCEPTION;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class CartItemService {
//
//    private final CartItemRepository cartItemRepository;
//    private final ItemRepository itemRepository;
//
//    public CreateCartItemResponseDTO addToCart(CreateCartItemRequestDTO dto) {
//        try {
//            Long userId = UserContextHolder.getUserContext().getUserId();
//            log.info("Adding item to cart. CustomerId: {}, ItemId: {}, Quantity: {}",userId, dto.getItemId(), dto.getQuantity());
//
//            ItemEntity item = itemRepository.findById(dto.getItemId())
//                    .orElseThrow(() -> {
//                        log.error("Item not found with id {}", dto.getItemId());
//                        return new ResourceNotFoundException("Item not found with id " + dto.getItemId());
//                    });
//
//            CartItemEntity cartItem = new CartItemEntity();
//            cartItem.setCustomerId(userId);
//            cartItem.setItem(item);
//            cartItem.setQuantity(dto.getQuantity());
//
//            CartItemEntity saved = cartItemRepository.save(cartItem);
//            log.info("Item saved in cart successfully. CartItemId: {}", saved.getId());
//            return CreateCartItemResponseDTO.fromEntity(saved);
//
//        } catch (Exception e) {
//            log.error("Unexpected error while adding item to cart", e);
//            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
//        }
//    }
//
//    public List<CreateCartItemResponseDTO> getCartItemsForCustomer() {
//        try {
//            Long userId = UserContextHolder.getUserContext().getUserId();
//            log.info("Fetching cart items for customerId: {}", userId);
//            List<CartItemEntity> cartItems = cartItemRepository.findByCustomerId(userId);
//
//            return cartItems.stream()
//                    .map(CreateCartItemResponseDTO::fromEntity)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            log.error("Unexpected error while fetching cart items", e);
//            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
//        }
//    }
//
//    public void removeCartItem(Long cartItemId) {
//        try {
//            Long userId = UserContextHolder.getUserContext().getUserId();
//            log.info("Attempting to remove cart item. CustomerId: {}, CartItemId: {}",userId, cartItemId);
//
//            CartItemEntity item = cartItemRepository.findById(cartItemId)
//                    .orElseThrow(() -> {
//                        log.error("Cart item not found with id: {}", cartItemId);
//                        return new ResourceNotFoundException("Cart Item not found");
//                    });
//
//            if (!item.getCustomerId().equals(userId)) {
//                log.warn("Unauthorized attempt to delete cart item. OwnerId: {}, RequesterId: {}", item.getCustomerId(),userId);
//                throw new UnauthorizedAccessException("You are not authorized to delete this cart item.");
//            }
//
//            cartItemRepository.delete(item);
//            log.info("Cart item with id {} deleted successfully for customerId: {}", cartItemId,userId);
//        } catch (Exception e) {
//            log.error("Unexpected error while removing cart item", e);
//            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
//        }
//    }
//
//    public CreateCartItemResponseDTO updateQuantity(Long cartItemId, int newQty) {
//        try {
//            Long userId = UserContextHolder.getUserContext().getUserId();
//            log.info("Request to update quantity. CartItemId: {}, NewQuantity: {}, CustomerId: {}", cartItemId, newQty,userId);
//
//            CartItemEntity item = cartItemRepository.findById(cartItemId)
//                    .orElseThrow(() -> {
//                        log.error("Cart item not found with id: {}", cartItemId);
//                        return new ResourceNotFoundException("Cart item not found");
//                    });
//
//            if (!item.getCustomerId().equals(userId)) {
//                log.warn("Unauthorized update attempt. OwnerId: {}, RequesterId: {}", item.getCustomerId(),userId);
//                throw new UnauthorizedAccessException("You are not authorized to update this cart item.");
//            }
//
//            item.setQuantity(newQty);
//            CartItemEntity updated = cartItemRepository.save(item);
//
//            log.info("Updated quantity for cart item. CartItemId: {}, NewQuantity: {}, CustomerId: {}", cartItemId, newQty,userId);
//            return CreateCartItemResponseDTO.fromEntity(updated);
//        } catch (Exception e) {
//            log.error("Unexpected error while updating cart item quantity", e);
//            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
//        }
//    }
//}
