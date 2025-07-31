package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.PlaceOrderRequestDTO;
import com.code.monks.nukkad.dto.response.PlaceOrderResponseDTO;
import com.code.monks.nukkad.entities.PlaceOrderEntity;
import com.code.monks.nukkad.services.PlaceOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import static com.code.monks.nukkad.constants.UrlConstants.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping(ITEMORDER.BASE)

public class PlaceOrderController
{
   private final PlaceOrderService placeOrderService;

    public PlaceOrderController(PlaceOrderService placeOrderService) {
        this.placeOrderService = placeOrderService;
    }

    @PostMapping(ITEMORDER.CREATE)
    public PlaceOrderResponseDTO createOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO)
   {
       log.info("Creating item order");
       return placeOrderService.createOrder(placeOrderRequestDTO);

   }

   @GetMapping(ITEMORDER.GET_ALL)
   public List<PlaceOrderEntity> getAllOrders()
   {
       log.info("Fetching al item orders");
       List<PlaceOrderEntity>orderEntities = placeOrderService.getAllOrders();
       log.info("Found {} item order (s)", getAllOrders().size());
       return placeOrderService.getAllOrders();
   }

   @DeleteMapping(ITEMORDER.DELETE_ORDER)
    public PlaceOrderEntity deleteOrder(@PathVariable Long id)
   {
       log.info("Deleting item order with ID:{}" , id);
       PlaceOrderEntity deletedOrder = placeOrderService.deleteOrder(id);
       log.info("Deleted item order :{}", deletedOrder);
       return deleteOrder(id);
   }

    @GetMapping (ITEMORDER.GET_ORDER_BY_ID)
    public PlaceOrderEntity getOrderById(@PathVariable Long id) {
        log.info("Fetching item order with IDv :{}" , id);
        PlaceOrderEntity orderEntity = placeOrderService.getOrderById(id);
        if(orderEntity == null)
        {
            log.warn("No item order found with ID :{}", id);
        }
        else {
            log.info("Found item order :{}" ,orderEntity);
        }
        return placeOrderService.getOrderById(id);

    }

}
