package com.neepanlokInfotech.nukkad_App.controllers;

import com.neepanlokInfotech.nukkad_App.dto.OrderRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.OrderResponseDTO;
import com.neepanlokInfotech.nukkad_App.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")

public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping ("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        OrderResponseDTO responseDTO = orderService.createDTO(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/getDetails")
    public ResponseEntity<List<OrderResponseDTO>> getOrderByStatus( @RequestParam String status)
    {
        List<OrderResponseDTO> order = orderService.getOrderByStatus(status);
        return ResponseEntity.ok(order);
    }


}
