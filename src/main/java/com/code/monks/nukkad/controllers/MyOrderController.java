
package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.response.*;
import com.code.monks.nukkad.enums.OrderStatusEnum;
import com.code.monks.nukkad.enums.OrderStatusFilterEnum;
import com.code.monks.nukkad.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

import static com.code.monks.nukkad.constants.UrlConstants.ORDER.*;

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
    public ResponseEntity<PagedOrderHistoryResponseDTO> getHistory(
            @RequestParam(required = false) OrderStatusEnum status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        PagedOrderHistoryResponseDTO response = orderService.getUserHistoryByOptionalFilters(
                status, startDate, endDate, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping(ORDER_COUNT_BY_STATUS)
    public ResponseEntity<OrderCountByStatusResponseDTO> getOrderCountsByStatus(
            @RequestParam(name = "order_status") OrderStatusEnum status
    ){
        OrderCountByStatusResponseDTO response = orderService.getOrderCountByStatus(status);
        return ResponseEntity.ok(response);
    }
}
