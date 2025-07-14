package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateCartItemRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateCartItemRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCartItemResponseDTO;
import com.code.monks.nukkad.dto.response.GetCartItemResponseDTO;
import com.code.monks.nukkad.dto.response.RemoveCartItemResponseDTO;
import com.code.monks.nukkad.dto.response.UpdateCartItemResponseDTO;
import com.code.monks.nukkad.services.CartItemService;
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
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping(CART_ITEM.ADD)
    public ResponseEntity<CreateCartItemResponseDTO> addToCart(@RequestBody CreateCartItemRequestDTO dto) {
        log.info("[ADD TO CART] Received request to add item: {}", dto);
        CreateCartItemResponseDTO response = cartItemService.addToCart(dto);
        log.info("[ADD TO CART] Item added successfully to cart. Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(CART_ITEM.GET_CART_ITEM_FOR_CUSTOMER)
    public ResponseEntity<List<GetCartItemResponseDTO>> getCartItems() {
        log.info("[GET CART ITEMS] Fetching cart items for customer");
        List<GetCartItemResponseDTO> items = cartItemService.getCartItemsForCustomer();
        log.info("[GET CART ITEMS] Found {} items in cart", items.size());
        return ResponseEntity.ok(items);
    }

    @DeleteMapping(CART_ITEM.DELETE_CART_ITEM_BY_ID)
    public ResponseEntity<RemoveCartItemResponseDTO> deleteCartItem(@PathVariable Long id) {
        log.info("[DELETE CART ITEM] Deleting cart item with ID: {}", id);
        RemoveCartItemResponseDTO resultMessage = cartItemService.removeCartItem(id);
        log.info("[DELETE CART ITEM] {}", resultMessage);
        return ResponseEntity.ok(resultMessage);
    }


    @PutMapping(CART_ITEM.UPDATE_QUANTITY_AND_UNIT_OF_ITEM)
    public ResponseEntity<UpdateCartItemResponseDTO> updateCartItem(@RequestBody UpdateCartItemRequestDTO dto) {
        log.info("Updating cart item: {}", dto);
        UpdateCartItemResponseDTO updated = cartItemService.updateCartItem(dto);
        return ResponseEntity.ok(updated);
    }


}
