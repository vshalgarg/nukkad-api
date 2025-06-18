package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.OrderRequestDTO;
import com.code.monks.nukkad.dto.response.OrderResponseDTO;
import com.code.monks.nukkad.services.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.code.monks.nukkad.constants.UrlConstants.*;
import static com.code.monks.nukkad.constants.UrlConstants.ORDER.*;

@Slf4j
@RestController
@RequestMapping(BASE)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(ORDER.CREATE)
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        OrderResponseDTO responseDTO = orderService.createOrders(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping(ORDER.CANCELLED_ORDER_BY_STOREKEEPER)
    public ResponseEntity<OrderResponseDTO> cancelOrderByStoreKeeper(
            @PathVariable Long id,
            @RequestParam String storeKeeperId) {
        OrderResponseDTO responseDTO = orderService.cancelOrderByStoreKeeper(id, storeKeeperId);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping(UPDATE_STATUS)
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderRequestDTO requestDTO
    ) {
        OrderResponseDTO responseDTO = orderService.updateOrderStatus(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

}