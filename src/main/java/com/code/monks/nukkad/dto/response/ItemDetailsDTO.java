package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailsDTO {
  private Long itemId;
  private String itemName;
  private String unit;
  private BigDecimal quantity;
  private Double price;
  private List<String> imageUrls;
}

