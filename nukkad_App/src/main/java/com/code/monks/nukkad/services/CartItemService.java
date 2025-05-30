package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.RequestContextHolder;
import com.code.monks.nukkad.dto.request.CreateCartItemRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCartItemResponseDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.entities.CartItemEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.CartItemRepository;
import com.code.monks.nukkad.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;

    public CreateCartItemResponseDTO addToCart(CreateCartItemRequestDTO dto){
        Long customerId = RequestContextHolder.getCustomer().getCustomerId();
        ItemEntity item = itemRepository.findById(dto.getItemId())
                .orElseThrow(()-> new ResourceNotFoundException("Item not found with id "+ dto.getItemId()));

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setCustomerId(customerId);
        cartItem.setItem(item);
        cartItem.setQuantity(dto.getQuantity());

        CartItemEntity saved = cartItemRepository.save(cartItem);
        return CreateCartItemResponseDTO.fromEntity(saved);
    }

    public List<CreateCartItemResponseDTO> getCartItemsForCustomer(){
        Long customerId = RequestContextHolder.getCustomer().getCustomerId();
        List<CartItemEntity> cartItems = cartItemRepository.findByCustomerId(customerId);

        return cartItems.stream()
                .map(CreateCartItemResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void removeCartItem(Long cartItemid){
        Long customerId = RequestContextHolder.getCustomer().getCustomerId();
        CartItemEntity item = cartItemRepository.findById(cartItemid)
                .orElseThrow(()-> new ResourceNotFoundException("Cart Item not found"));

        if (!item.getCustomerId().equals(customerId)){
            throw new RuntimeException("Unauthorized");
        }

        cartItemRepository.delete(item);
    }
    public CreateCartItemResponseDTO updateQuantity(Long cartItemId, int newQty) {
        Long customerId = RequestContextHolder.getCustomer().getCustomerId();
        CartItemEntity item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getCustomerId().equals(customerId)) {
            throw new RuntimeException("Unauthorized");
        }

        item.setQuantity(newQty);
        return CreateCartItemResponseDTO.fromEntity(cartItemRepository.save(item));
    }

}
