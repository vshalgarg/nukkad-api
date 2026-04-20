package com.code.monks.nukkad.services;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.repositories.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class BatchPersistenceService {

    private final ItemRepository itemRepository;

    @PersistenceContext
    private final EntityManager entityManager;
    @Transactional
    public void saveBatch(List<ItemEntity> products) {
        log.info("[BATCH PERSISTENCE] Saving batch of {} products", products.size());
        itemRepository.saveAll(products); // saves products + cascades to images
        entityManager.flush();            // SQL database sync SQL
        entityManager.clear();           // free memory — evict all from cache
        log.info("[BATCH PERSISTENCE] Batch saved successfully — {} products",
                products.size());
    }

    @Transactional
    public CategoryEntity reattachCategory(CategoryEntity detachedCategory) {
        return entityManager.merge(detachedCategory);
    }}
