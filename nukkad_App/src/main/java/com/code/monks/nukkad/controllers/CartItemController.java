package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateCartItemRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCartItemResponseDTO;
import com.code.monks.nukkad.services.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartItem")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping("/add")
    public ResponseEntity<CreateCartItemResponseDTO> addToCart(@RequestBody CreateCartItemRequestDTO dto){
        CreateCartItemResponseDTO saveItemInCart = cartItemService.addToCart(dto);

        return new  ResponseEntity<>(saveItemInCart, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CreateCartItemResponseDTO>> getCartItems() {
        return ResponseEntity.ok(cartItemService.getCartItemsForCustomer());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.removeCartItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<CreateCartItemResponseDTO> updateQuantity(@PathVariable Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(cartItemService.updateQuantity(id, quantity));
    }


}
