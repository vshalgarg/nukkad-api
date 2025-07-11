
package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.response.GetOrderByStoreKeeperResponseDTO;
import com.code.monks.nukkad.dto.response.GetUserHistoryByStatusAndDateResponseDTO;
import com.code.monks.nukkad.enums.Status;
import com.code.monks.nukkad.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<GetOrderByStoreKeeperResponseDTO>> getOrdersByStorekeeperId() {


        List<GetOrderByStoreKeeperResponseDTO> response =
                orderService.getOrdersByStorekeeper();
        log.info("Returning {}", response.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping(ORDER_HISTORY)
    public ResponseEntity<List<GetUserHistoryByStatusAndDateResponseDTO>> getHistory(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<GetUserHistoryByStatusAndDateResponseDTO> response =
                orderService.getUserHistoryByOptionalFilters(status, startDate, endDate);
        return ResponseEntity.ok(response);
    }


}
