package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.CreateRatingRequestDTO;
import com.code.monks.nukkad.dto.response.CreateRatingResponseDTO;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.RatingEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.RatingRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService
{
    private final RatingRepository ratingRepository;
    private final CustomerRepository customerRepository;
    private final StorekeeperRepository storekeeperRepository;

    public CreateRatingResponseDTO createRating(CreateRatingRequestDTO dto)
    {
        log.info("[Create Rating] Received request:{}", dto);
        CustomerEntity customerEntity = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(()->
                {
                    log.error("[CREATE RATING] Customer not found with Id:{}"+ dto.getCustomerId());

                    return new ResourceNotFoundException(ResponseErrorCodes.CUSTOMER_NOT_FOUND, dto.getCustomerId());
                });

        StorekeeperEntity storekeeperEntity =storekeeperRepository.findById(dto.getStoreKeeperId())
                .orElseThrow(()->
                {
                   log.error("[CREATE RATING] StoreKeeper not found with id:{}"+dto.getStoreKeeperId());

                   return new ResourceNotFoundException(ResponseErrorCodes.STOREKEEPER_NOT_FOUND, dto.getStoreKeeperId());
                });

        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setCustomer(customerEntity);
        ratingEntity.setStorekeeper(storekeeperEntity);
        ratingEntity.setReview(dto.getReview());  // ✅ set from DTO
        ratingEntity.setRating(dto.getRating());

        RatingEntity saved = ratingRepository.save(ratingEntity);

        log.info("[CREATE RATING] Rating successfully saved with ID: {}", saved.getId());

        CreateRatingResponseDTO response = CreateRatingResponseDTO.toResponseDTO(saved);
        response.setMessage("Review submitted successfully!"); // Add success message
        return response;
    }
}
