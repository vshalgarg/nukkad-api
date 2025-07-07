package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.dto.response.DispatchItemDTO;
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
    private List<DispatchItemDTO> orderItem;
    private String  note;
}
