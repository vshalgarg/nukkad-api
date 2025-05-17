package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.dto.response.CreateItemResponseDTO;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class CreateItemRequestDTO {

	private String name;

	private String image;

	private String unit;

	private List<Long> categoryIds;


}
