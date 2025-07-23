package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.response.SearchResponseDTO;
import com.code.monks.nukkad.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.code.monks.nukkad.constants.UrlConstants.SEARCH;

@Slf4j
@RestController
@RequestMapping(SEARCH.BASE)
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping(SEARCH.GET)
    public ResponseEntity<SearchResponseDTO> search(@RequestParam String keyword) {
        log.info("[SEARCH] Request received with keyword: {}", keyword);

        SearchResponseDTO response = searchService.search(keyword);

        return ResponseEntity.ok(response);
    }
}
