package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.response.CreateOrderHistoryResponseDTO;
import com.code.monks.nukkad.dto.response.OrderResponseDTO;
import com.code.monks.nukkad.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.ORDER.*;

@RequestMapping(UrlConstants.ORDER.BASE)
@RestController
@Slf4j
@RequiredArgsConstructor

public class MyOrderController
{
    @Autowired
     private OrderService orderService;
    
    @GetMapping(GET_ALL)
    public ResponseEntity<List<OrderResponseDTO>> getOrderByStatus(@RequestParam String status) {
       log.info("Fetching orders with status :{}", status);
        List<OrderResponseDTO> order = orderService.getOrderByStatus(status);
        log.info("Found {} order(s) with status : {}", order.size(), status);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping(GET_ALL_ORDER_BY_ID)
    public ResponseEntity<List<OrderResponseDTO>> getAllOrderById(@PathVariable int id) {
        log.info("Fetching order by ID: {}", id);
        OrderService orderService = null;
        List<OrderResponseDTO> responseDTO =orderService.getAllOrderById(id);
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping(ORDER_HISTORY)
    public ResponseEntity<List<CreateOrderHistoryResponseDTO>> getOrderHistory(
            @RequestParam String status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<CreateOrderHistoryResponseDTO> response = orderService.getOrderHistory(status, date);
        return ResponseEntity.ok(response);
    }


//    @GetMapping("/filter")
//    public List<OrderResponseDTO> filterOrder(
//            @RequestParam(required = false) Integer customerId,
//            @RequestParam(required = false) Integer storeKeeperId) {
//
//        return OrderService.filterOrders(customerId, storeKeeperId);
//    }
}
