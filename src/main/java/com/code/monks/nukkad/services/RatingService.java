
package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateRatingRequestDTO;
import com.code.monks.nukkad.dto.response.CreateRatingResponseDTO;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.RatingEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.RatingRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final CustomerRepository customerRepository;
    private final StorekeeperRepository storekeeperRepository;

    public CreateRatingResponseDTO createRating(CreateRatingRequestDTO dto) {
        log.info("[CREATE RATING] Request received: {}", dto);

        Long customerId = UserContextHolder.getUser().getId();
        log.debug("[CREATE RATING] Authenticated customer ID: {}", customerId);

        // Validate Customer
        CustomerEntity customerEntity = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("[CREATE RATING] Customer not found. ID: {}", customerId);
                    return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
                });

        // Validate Storekeeper
        Long storekeeperId = dto.getStoreKeeperId();
        StorekeeperEntity storekeeperEntity = storekeeperRepository.findById(storekeeperId)
                .orElseThrow(() -> {
                    log.error("[CREATE RATING] Storekeeper not found. ID: {}", storekeeperId);
                    return new ResourceNotFoundException(STOREKEEPER_NOT_FOUND, storekeeperId);
                });

        // Save Rating
        try {
            RatingEntity ratingEntity = new RatingEntity();
            ratingEntity.setCustomer(customerEntity);
            ratingEntity.setStorekeeper(storekeeperEntity);
            ratingEntity.setReview(dto.getReview());
            ratingEntity.setRating(dto.getRating());

            RatingEntity savedRating = ratingRepository.save(ratingEntity);
            log.info("[CREATE RATING] Rating saved successfully. Rating ID: {}", savedRating.getId());

            return new CreateRatingResponseDTO("Review submitted successfully!");
        } catch (Exception e) {
            log.error("[CREATE RATING] Failed to save rating for customer ID: {} and storekeeper ID: {}",
                    customerId, storekeeperId, e);
            throw new UnhandledException(SUBMIT_RATING_EXCEPTION,e);
        }
    }
}

