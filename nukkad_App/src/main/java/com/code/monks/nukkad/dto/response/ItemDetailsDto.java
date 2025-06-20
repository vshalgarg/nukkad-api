package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.enums.UnitEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailsDto {
  private int itemId;
  private String itemName;
  private UnitEnum unit;
}
