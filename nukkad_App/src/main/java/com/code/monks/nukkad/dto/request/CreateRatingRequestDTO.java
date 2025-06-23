package com.code.monks.nukkad.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRatingRequestDTO
{
    @NotNull
    private Long customerId;

    @NotNull
    private Long storeKeeperId;

    @NotBlank(message = "Review must not be null")
    private String review;

    @NotNull(message = "Rating must not be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")

    private int rating;
}
