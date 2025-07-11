package com.code.monks.nukkad.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DispatchOrderRequestDTO
{
    private Long orderId;
    private List<DispatchItemRequestDTO> orderItem;
    private String note;
}
