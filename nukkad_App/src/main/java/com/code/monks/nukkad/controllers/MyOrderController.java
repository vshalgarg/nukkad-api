package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.response.MyOrderResponseDTO;
import com.code.monks.nukkad.services.MyOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/myOrder")
public class MyOrderController
{
     private MyOrderService myOrderService;

     @GetMapping("/filter")
     public List<MyOrderResponseDTO> filterOrder(@RequestBody MyOrderResponseDTO request)
     {
         List<MyOrderResponseDTO> response =  myOrderService.filterOrders(request.getCustomerId(),request.getStoreKeeperId());
         return response;
     }

}
