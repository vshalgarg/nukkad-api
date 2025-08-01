package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrdersResponseDTO {
    private List<GetOrderByStoreKeeperResponseDTO> orders;
    private long totalOrders;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}
