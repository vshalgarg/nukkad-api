package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.PlaceOrderRequestDTO;
import com.code.monks.nukkad.dto.response.PlaceOrderResponseDTO;
import com.code.monks.nukkad.services.PlaceOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.PLACEORDER;
@Slf4j
@RestController
@RequestMapping(PLACEORDER.BASE)

public class PlaceOrderController {
    private final PlaceOrderService placeOrderService;

    public PlaceOrderController(PlaceOrderService placeOrderService) {
        this.placeOrderService = placeOrderService;
    }

    @PostMapping(PLACEORDER.CREATE)
    public PlaceOrderResponseDTO createOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO) {
        log.info("Creating item order");
        return placeOrderService.createOrder(placeOrderRequestDTO);

    }

    @GetMapping(PLACEORDER.GET_ALL)
    public List<PlaceOrderResponseDTO> getAllOrders() {
        log.info("Fetching al item orders");
        List<PlaceOrderResponseDTO> responseDTO = placeOrderService.getAllOrders();
        return placeOrderService.getAllOrders();
    }

    @DeleteMapping(PLACEORDER.DELETE_ORDER)
    public String deleteOrder(@RequestParam int id) {
        log.info("Deleting item order with ID:{}", id);
        String deletedOrder = placeOrderService.deleteOrder(id);
        return deletedOrder;
    }

    @GetMapping(PLACEORDER.GET_ORDER_BY_ID)
    public PlaceOrderResponseDTO getOrderById(@RequestParam("OrderId") int id) {
        log.info("Fetching item order with IDv :{}", id);
        PlaceOrderResponseDTO responseDTO = placeOrderService.getOrderById(id);
        return placeOrderService.getOrderById(id);
    }
}
