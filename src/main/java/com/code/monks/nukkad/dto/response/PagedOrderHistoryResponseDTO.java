package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedOrderHistoryResponseDTO {
    private List<GetUserHistoryByStatusAndDateResponseDTO> orders;
    private long totalOrders;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}
