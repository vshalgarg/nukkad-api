package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.RatingEntity;
import lombok.Data;

@Data
public class CreateRatingResponseDTO
{
    private String message;

    public static CreateRatingResponseDTO toResponseDTO(RatingEntity entity) {
        CreateRatingResponseDTO responseDTO = new CreateRatingResponseDTO();
        responseDTO.setMessage("Review submitted successfully!");  // ✅ Set message
        return responseDTO;
    }
}
