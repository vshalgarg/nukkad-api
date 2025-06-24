
package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.request.CreateRatingRequestDTO;
import com.code.monks.nukkad.dto.response.CreateRatingResponseDTO;
import com.code.monks.nukkad.services.RatingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.code.monks.nukkad.constants.UrlConstants.RATING.BASE;
import static com.code.monks.nukkad.constants.UrlConstants.RATING.CREATE;

@Slf4j
@RestController
@RequestMapping(BASE)
public class RatingController
{
    @Autowired
    private RatingService service;

    @PostMapping(CREATE)
    public ResponseEntity<CreateRatingResponseDTO> createRating(@Valid @RequestBody CreateRatingRequestDTO dto)
    {
        log.info("[API] Creating new rating: {}", dto);

        CreateRatingResponseDTO response = service.createRating(dto);
        return ResponseEntity.ok(response);
    }
}

