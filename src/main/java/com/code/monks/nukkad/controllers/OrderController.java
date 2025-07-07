
package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.DispatchOrderRequestDTO;
import com.code.monks.nukkad.dto.request.OrderRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateOrderStatusRequestDTO;
import com.code.monks.nukkad.dto.response.*;
import com.code.monks.nukkad.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.code.monks.nukkad.constants.UrlConstants.*;
import static com.code.monks.nukkad.constants.UrlConstants.ORDER.*;

@Slf4j
@RestController
@RequestMapping(BASE)
@AllArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(PLACE_ORDER )
    public ResponseEntity<PlaceOrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO requestDTO) {
        PlaceOrderResponseDTO response = orderService.placeOrders(requestDTO);
        return ResponseEntity.ok(response);
    }


    @PostMapping(ORDER.CANCELLED_ORDER_BY_STOREKEEPER)
    public ResponseEntity<CancelOrderByStoreKeeperResponseDTO> cancelOrderByStoreKeeper(
            @PathVariable Long id)
    {
        log.info("Request to cancel orderId={}", id);

        CancelOrderByStoreKeeperResponseDTO responseDTO = orderService.cancelOrderByStoreKeeper(id);

        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping(UPDATE_STATUS)
    public ResponseEntity<UpdateOrderStatusResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusRequestDTO requestDTO
    )
    {
        log.info("Updating orderId={} to status={}", id, requestDTO.getStatus());

        UpdateOrderStatusResponseDTO responseDTO = orderService.updateOrderStatus(id, requestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping(REPEAT_ORDER)
    public ResponseEntity<RepeatOrderResponseDTO> repeatOrder(@PathVariable Long id)
    {
        RepeatOrderResponseDTO response = orderService.repeatOrder(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping(DISPATCH_ORDER)
    public ResponseEntity<DispatchOrderResponseDTO> dispatchOrder(@RequestBody DispatchOrderRequestDTO request) {
        DispatchOrderResponseDTO response = orderService.dispatchOrder(request);
        return ResponseEntity.ok(response);
    }

}
