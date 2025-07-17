package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailsDTO {
  private Long itemId;
  private String itemName;
  private String unit;
  private int quantity;
  private double price;
  private List<String> imageUrls;
}

