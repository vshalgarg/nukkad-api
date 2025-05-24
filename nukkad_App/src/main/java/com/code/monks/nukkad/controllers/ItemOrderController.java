package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.ItemOrderRequestDTO;
import com.code.monks.nukkad.dto.response.ItemOrderResponseDTO;
import com.code.monks.nukkad.entities.ItemOrderEntity;
import com.code.monks.nukkad.services.ItemOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storeKeeper")

public class ItemOrderController
{
   private final ItemOrderService itemOrderService;

    public ItemOrderController(ItemOrderService itemOrderService) {
        this.itemOrderService = itemOrderService;
    }

    @PostMapping
    public ItemOrderResponseDTO createOrder(@RequestBody ItemOrderRequestDTO itemOrderRequestDTO)
   {
       return itemOrderService.createOrder(itemOrderRequestDTO);
   }

   @GetMapping
   public List<ItemOrderEntity> getAllOrders()
   {
       return itemOrderService.getAllOrders();
   }

   @GetMapping
    public ItemOrderEntity getOrderById(@PathVariable Long id) {
       return itemOrderService.getOrderById(id);

   }

   @DeleteMapping
    public ItemOrderEntity deleteOrder(@PathVariable Long id)
   {
       service.deleteOrder(id);
   }

}
