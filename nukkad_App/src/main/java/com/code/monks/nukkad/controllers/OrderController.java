//package com.code.monks.nukkad.controllers;
//
//import com.code.monks.nukkad.dto.request.OrderRequestDTO;
//import com.code.monks.nukkad.dto.response.OrderResponseDTO;
//import com.code.monks.nukkad.services.OrderService;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import static com.code.monks.nukkad.constants.UrlConstants.*;
//
//import java.util.List;
//@Slf4j
//@RestController
//@RequestMapping(ORDER.BASE)
//public class OrderController {
//    @Autowired
//    private OrderService orderService;
//
//    @PostMapping(ORDER.CREATE)
//    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
//        log.info("Creating new order for customer :{}", requestDTO.getOrderId());
//        OrderResponseDTO responseDTO = orderService.createDTO(requestDTO);
//        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
//    }
//
//    @GetMapping(ORDER.GET_ALL)
//    public ResponseEntity<List<OrderResponseDTO>> getOrderByStatus(@RequestParam String status) {
//        log.info("Fetching orders with status :{}", status);
//        List<OrderResponseDTO> order = orderService.getOrderByStatus(status);
//        log.info("Found {} order(s) with status : {}", order.size(), status);
//        return ResponseEntity.ok(order);
//    }
//
//    @GetMapping(ORDER.GET_BY_TRACKING)
//    public ResponseEntity<List<OrderResponseDTO>> getOrderByTrackingNumber(@RequestParam String trackingNumber) {
//        log.info("Fetching orders with tracking number : {}", trackingNumber);
//        List<OrderResponseDTO> order = orderService.getOrderByTrackingNumber(trackingNumber);
//        log.info("Found {} order(s) for tracking number :{}", order.size(), trackingNumber);
//        return ResponseEntity.ok(order);
//    }
//
//}