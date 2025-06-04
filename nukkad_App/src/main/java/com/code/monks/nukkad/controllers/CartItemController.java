//package com.code.monks.nukkad.controllers;
//
//import com.code.monks.nukkad.dto.request.CreateCartItemRequestDTO;
//import com.code.monks.nukkad.dto.response.CreateCartItemResponseDTO;
//import com.code.monks.nukkad.services.CartItemService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//import static com.code.monks.nukkad.constants.UrlConstants.CART_ITEM;
//
//@RestController
//@RequestMapping(CART_ITEM.BASE)
//@RequiredArgsConstructor
//public class CartItemController {
//
//    private final CartItemService cartItemService;
//
//    @PostMapping(CART_ITEM.ADD)
//    public ResponseEntity<CreateCartItemResponseDTO> addToCart(@RequestBody CreateCartItemRequestDTO dto){
//        CreateCartItemResponseDTO saveItemInCart = cartItemService.addToCart(dto);
//
//        return new  ResponseEntity<>(saveItemInCart, HttpStatus.CREATED);
//    }
//
//    @GetMapping(CART_ITEM.GET_CART_ITEM_FOR_CUSTOMER)
//    public ResponseEntity<List<CreateCartItemResponseDTO>> getCartItems() {
//        return ResponseEntity.ok(cartItemService.getCartItemsForCustomer());
//    }
//
//
//    @DeleteMapping(CART_ITEM.DELETE_CART_ITEM_BY_ID)
//    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
//        cartItemService.removeCartItem(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping(CART_ITEM.UPDATE_QUANTITY)
//    public ResponseEntity<CreateCartItemResponseDTO> updateQuantity(@PathVariable Long id, @RequestParam int quantity) {
//        return ResponseEntity.ok(cartItemService.updateQuantity(id, quantity));
//    }
//
//
//}
