package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.response.CreateCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.CreateItemResponseDTO;
import com.code.monks.nukkad.dto.response.SearchResponseDTO;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.CategoryRepository;
import com.code.monks.nukkad.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.SEARCH_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    public SearchResponseDTO search(String keyword) {
        log.info("Starting search for keyword: '{}'", keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("Search keyword is null or empty");
            return new SearchResponseDTO(Collections.emptyList(), Collections.emptyList());
        }

        try {
            Pageable pageable = Pageable.unpaged(); // Use Pageable.of(page, size) if you want actual pagination
            Page<CategoryEntity> categoryPage = categoryRepository.findAllByNameContainingIgnoreCase(keyword, pageable);
            Page<ItemEntity> itemPage = itemRepository.findByNameContainingIgnoreCase(keyword, pageable);

            List<CreateCategoryResponseDTO> categoryDTOs = categoryPage.getContent().stream()
                    .map(CreateCategoryResponseDTO::fromEntity)
                    .toList();

            List<CreateItemResponseDTO> itemDTOs = itemPage.getContent().stream()
                    .map(CreateItemResponseDTO::fromEntity)
                    .toList();

            log.info("Returning {} categoryDTOs and {} itemDTOs", categoryDTOs.size(), itemDTOs.size());

            return new SearchResponseDTO(categoryDTOs, itemDTOs);
        } catch (Exception e) {
            log.error("Error occurred during search for keyword '{}': {}", keyword, e.getMessage(), e);
            throw new UnhandledException(SEARCH_NOT_FOUND, e);
        }
    }
}
