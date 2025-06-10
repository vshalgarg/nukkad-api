//package com.code.monks.nukkad.controllers;
//
//import ch.qos.logback.classic.Logger;
//import com.code.monks.nukkad.dto.request.MyOrderRequestDTO;
//import com.code.monks.nukkad.dto.response.MyOrderResponseDTO;
//import com.code.monks.nukkad.entities.MyOrderEntity;
//import com.code.monks.nukkad.services.MyOrderService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/myOrder")
//public class MyOrderController
//{
//    @Autowired
//     private MyOrderService myOrderService;
//    @PostMapping("/create")
//    public ResponseEntity<MyOrderResponseDTO> createMyOrder(@Valid @RequestBody MyOrderRequestDTO requestDTO)
//    {
//        MyOrderEntity myOrderEntity = MyOrderRequestDTO.toEntity(requestDTO); // You must implement this
//        MyOrderResponseDTO responseDTO = myOrderService.createMyOrder(myOrderEntity);
//        return ResponseEntity.ok(responseDTO);
//    }
//
//    @GetMapping("/filter")
//    public List<MyOrderResponseDTO> filterOrder(
//            @RequestParam(required = false) Integer customerId,
//            @RequestParam(required = false) Integer storeKeeperId) {
//
//        return myOrderService.filterOrders(customerId, storeKeeperId);
//    }
//}
