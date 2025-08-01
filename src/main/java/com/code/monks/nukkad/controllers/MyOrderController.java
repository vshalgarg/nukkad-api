
package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.response.GetOrderByStoreKeeperResponseDTO;
import com.code.monks.nukkad.dto.response.GetOrdersResponseDTO;
import com.code.monks.nukkad.dto.response.GetUserHistoryByStatusAndDateResponseDTO;
import com.code.monks.nukkad.enums.OrderStatusEnum;
import com.code.monks.nukkad.enums.OrderStatusFilterEnum;
import com.code.monks.nukkad.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;
import static com.code.monks.nukkad.constants.UrlConstants.ORDER.GET_ORDER_BY_STOREKEEPER;
import static com.code.monks.nukkad.constants.UrlConstants.ORDER.ORDER_HISTORY;

@RequestMapping(UrlConstants.ORDER.BASE)
@RestController
@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
public class MyOrderController
{
    @Autowired
    private OrderService orderService;

    @GetMapping(GET_ORDER_BY_STOREKEEPER)
    public ResponseEntity<GetOrdersResponseDTO> getOrdersByStorekeeperId(
            @RequestParam OrderStatusFilterEnum statusFilter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        GetOrdersResponseDTO response = orderService.getOrdersByStorekeeper(statusFilter, page, size);
        log.info("Returning {} orders for status={} on page={}", response.getTotalOrders(), statusFilter, page);
        return ResponseEntity.ok(response);
    }



    @GetMapping(ORDER_HISTORY)
    public ResponseEntity<List<GetUserHistoryByStatusAndDateResponseDTO>> getHistory(
            @RequestParam(required = false) OrderStatusEnum status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        List<GetUserHistoryByStatusAndDateResponseDTO> response =
                orderService.getUserHistoryByOptionalFilters(status, startDate, endDate, minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }



}
