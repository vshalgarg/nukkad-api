package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailsDTO {
  private Long itemId;
  private String itemName;
  private String unit;
  private int quantity;
  private double price;
}
