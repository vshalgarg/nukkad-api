package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStorekeeperResponseDTO {

	private int id;

	private String name;

	private String storeNumber;

	private String gstIn;

	private String address;

	private String city;

	private List<String> pictures;

	public static CreateStorekeeperResponseDTO fromDbDto(StorekeeperEntity entity) {
		return new CreateStorekeeperResponseDTO(
				entity.getId(),
				entity.getName(),
				entity.getStoreNumber(),
				entity.getGstIn(),
				entity.getAddress(),
				entity.getCity(),
				entity.getPictures()
		);
	}
}

