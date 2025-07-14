package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateCartProductRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateCartProductRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCartProductResponseDTO;
import com.code.monks.nukkad.dto.response.GetCartProductResponseDTO;
import com.code.monks.nukkad.dto.response.UpdateCartProductResponseDto;
import com.code.monks.nukkad.dto.response.removeCartProductResponseDTO;
import com.code.monks.nukkad.services.CartProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.CART_ITEM;

@Slf4j
@RestController
@RequestMapping(CART_ITEM.BASE)
@RequiredArgsConstructor
public class CartProductController {

    private final CartProductService cartItemService;

    @PostMapping(CART_ITEM.ADD)
    public ResponseEntity<CreateCartProductResponseDTO> addToCart(@RequestBody CreateCartProductRequestDTO dto) {
        log.info("[ADD TO CART] Received request to add item: {}", dto);
        CreateCartProductResponseDTO response = cartItemService.addToCart(dto);
        log.info("[ADD TO CART] Item added successfully to cart. Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(CART_ITEM.GET_CART_ITEM_FOR_CUSTOMER)
    public ResponseEntity<List<GetCartProductResponseDTO>> getCartItems() {
        log.info("[GET CART ITEMS] Fetching cart items for customer");
        List<GetCartProductResponseDTO> items = cartItemService.getCartItemsForCustomer();
        log.info("[GET CART ITEMS] Found {} items in cart", items.size());
        return ResponseEntity.ok(items);
    }

    @DeleteMapping(CART_ITEM.DELETE_CART_ITEM_BY_ID)
    public ResponseEntity<removeCartProductResponseDTO> deleteCartItem(@PathVariable Long id) {
        log.info("[DELETE CART ITEM] Deleting cart item with ID: {}", id);
        removeCartProductResponseDTO resultMessage = cartItemService.removeCartItem(id);
        log.info("[DELETE CART ITEM] {}", resultMessage);
        return ResponseEntity.ok(resultMessage);
    }


    @PutMapping(CART_ITEM.UPDATE_QUANTITY_AND_UNIT_OF_PRODUCT)
    public ResponseEntity<UpdateCartProductResponseDto> updateCartItem(@RequestBody UpdateCartProductRequestDTO dto) {
        log.info("Updating cart item: {}", dto);
        UpdateCartProductResponseDto updated = cartItemService.updateCartProduct(dto);
        return ResponseEntity.ok(updated);
    }


}
