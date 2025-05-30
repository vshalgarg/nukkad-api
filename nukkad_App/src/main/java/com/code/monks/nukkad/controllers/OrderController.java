//package com.code.monks.nukkad.controllers;
//
//import com.code.monks.nukkad.constants.UrlConstants;
//import com.code.monks.nukkad.dto.request.OrderRequestDTO;
//import com.code.monks.nukkad.dto.response.OrderResponseDTO;
//import com.code.monks.nukkad.services.OrderService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping(UrlConstants.ORDER.BASE)
//public class OrderController
//{
//    @Autowired
//    private OrderService orderService;
//
//    @PostMapping(UrlConstants.ORDER.CREATE)
//    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
//        OrderResponseDTO responseDTO = orderService.createDTO(requestDTO);
//        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
//    }
//
//    @GetMapping(UrlConstants.ORDER.GET_ALL)
//    public ResponseEntity<List<OrderResponseDTO>> getOrderByStatus( @RequestParam String status)
//    {
//        List<OrderResponseDTO> order = orderService.getOrderByStatus(status);
//        return ResponseEntity.ok(order);
//    }
//
//    @GetMapping(UrlConstants.ORDER.GET_BY_TRACKING)
//    public ResponseEntity<List<OrderResponseDTO>> getOrderByTrackingNumber(@RequestParam String trackingNumber)
//    {
//        List<OrderResponseDTO> order= orderService.getOrderByTrackingNumber(trackingNumber);
//        return ResponseEntity.ok(order);
//    }
//}
