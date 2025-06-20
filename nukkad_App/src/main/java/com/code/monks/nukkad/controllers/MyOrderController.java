package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.response.CreateCustomerOrderResponseDTO;
import com.code.monks.nukkad.dto.response.CreateOrderHistoryResponseDTO;
import com.code.monks.nukkad.dto.response.CreateStoreKeeperOrderResponseDTO;
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
    
    @GetMapping(GET_ALL_STATUS)
    public ResponseEntity<List<OrderResponseDTO>> getOrderByStatus(@RequestParam String status) {
       log.info("Fetching orders with status :{}", status);
        List<OrderResponseDTO> order = orderService.getOrderByStatus(status);
        log.info("Found {} order(s) with status : {}", order.size(), status);
        return ResponseEntity.ok(order);
    }


    @GetMapping(GET_ORDER_BY_STOREKEEPER)
    public ResponseEntity<List<CreateCustomerOrderResponseDTO>> getOrdersByStorekeeperId(
            @RequestParam("storeKeeperId") Long storeKeeperId) {

        log.info("Received request to fetch orders for storeKeeperId={}", storeKeeperId);

        List<CreateCustomerOrderResponseDTO> response =
                orderService.getOrdersByStorekeeperId(storeKeeperId);
        log.info("Returning {} order(s) for storeKeeperId={}", response.size(), storeKeeperId);

        return ResponseEntity.ok(response);
    }

      @GetMapping(GET_ORDER_BY_CUSTOMER)
    public ResponseEntity<List<CreateStoreKeeperOrderResponseDTO>> getStoreKeeperInfoByCustomerId
            (@RequestParam("customerId") Long customerId)
    {
        log.info("Received request to fetch storekeeper info for customerId={}", customerId);

        List<CreateStoreKeeperOrderResponseDTO> responseDTO =
                orderService.getStoreKeeperInfoByCustomerId(customerId);
        log.info("Returning {} storekeeper order(s) for customerId={}", responseDTO.size(), customerId);

        return ResponseEntity.ok(responseDTO);
    }

//    @GetMapping(GET_ALL_ORDER_BY_ID)
//    public ResponseEntity<List<OrderResponseDTO>> getAllOrderById(@PathVariable int id) {
//        log.info("Fetching order by ID: {}", id);
//        OrderService orderService = null;
//        List<OrderResponseDTO> responseDTO =orderService.getAllOrderById(id);
//        return ResponseEntity.ok(responseDTO);
//    }


    @GetMapping(ORDER_HISTORY)
    public ResponseEntity<List<CreateOrderHistoryResponseDTO>> getOrderHistory(
            @RequestParam String status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        log.info("Received request to fetch order history with status='{}' and date='{}'", status, date);

        List<CreateOrderHistoryResponseDTO> response = orderService.getOrderHistory(status, date);
        log.info("Returning {} order history record(s) for status='{}' on date='{}'",
                response.size(), status, date);

        return ResponseEntity.ok(response);
    }

}
