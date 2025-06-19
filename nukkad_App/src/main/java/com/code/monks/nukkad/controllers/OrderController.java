package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateOrderRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateOrderRequestDTO;
import com.code.monks.nukkad.dto.response.OrderResponseDTO;
import com.code.monks.nukkad.dto.response.StatusResponseDTO;
import com.code.monks.nukkad.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusResponseDTO> updateOrder(
            @PathVariable Long id,
            @RequestBody UpdateOrderRequestDTO dto
    ) {
        return ResponseEntity.ok(orderService.updateOrder(id, dto));
    }

}
