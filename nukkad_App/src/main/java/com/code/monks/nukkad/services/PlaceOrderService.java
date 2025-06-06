package com.code.monks.nukkad.services;
import com.code.monks.nukkad.dto.request.PlaceOrderRequestDTO;
import com.code.monks.nukkad.dto.response.PlaceOrderResponseDTO;
import com.code.monks.nukkad.entities.PlaceOrderEntity;
import com.code.monks.nukkad.enums.PlaceOrderEnum;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.ItemRepository;
import com.code.monks.nukkad.repositories.OrderRepository;
import com.code.monks.nukkad.repositories.PlaceOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderService
{
    private final PlaceOrderRepository placeOrderRepository;

    private final ItemRepository itemRepository;

    private final OrderRepository orderRepository;

    @Transactional
    public PlaceOrderResponseDTO createOrder(PlaceOrderRequestDTO request) {
        log.info("Attempting to create new place order with itemId={}, orderId={}",
                request.getItemId(), request.getOrderId());

        try {
            PlaceOrderEntity entity = new PlaceOrderEntity();

            // Correct setters using entity objects
            entity.setItem(itemRepository.findById(request.getItemId())
                    .orElseThrow(() ->
                    {
                        log.warn("Item not found with ID: {}", request.getItemId());
                        return new ResourceNotFoundException("Item not found with ID :"+request.getItemId());
                    }));
            entity.setOrder(orderRepository.findById(request.getOrderId())
                    .orElseThrow(() ->
                    {
                        log.warn("Order not found with ID: {}", request.getOrderId());
                        return new ResourceNotFoundException("Order not found with ID :"+request.getOrderId());
                    }));

            entity.setStatus(request.getStatus() != null ? request.getStatus() : PlaceOrderEnum.PENDING);
            entity.setQuantity(request.getQuantity());

            PlaceOrderEntity saved = placeOrderRepository.save(entity);
            log.info("Place order created successfully with ID: {}", saved.getId());

            return PlaceOrderResponseDTO.mapToResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            log.error("Duplicate entry error while saving order: {}", ex.getMessage(), ex);
            throw new DuplicateResourceException(ResponseErrorCodes.DUPLICATE_CATEGORY_EXCEPTION);
        } catch (Exception ex) {
            log.error("Failed to save place order: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    public List<PlaceOrderResponseDTO> getAllOrders()
    {
        log.info("Fetching all orders");
        List<PlaceOrderEntity> entityList = placeOrderRepository.findAll();
        return entityList.stream().map(PlaceOrderResponseDTO::mapToResponse)
                .collect(Collectors.toList());
    }

    public PlaceOrderResponseDTO getOrderById(int id)
    {
        log.info("Fetching order with ID: {}", id);

        PlaceOrderEntity entity = placeOrderRepository.findByOrderId(id).orElseThrow(()->
        {
            log.warn("Place order not found with ID: {}", id);
            return new ResourceNotFoundException("Place order not found with ID: " + id);

        });
        return PlaceOrderResponseDTO.mapToResponse(entity);
    }

    public String deleteOrder(int id)
    {
        log.info("Deleting order with ID: {}", id);

        if (!placeOrderRepository.existsByOrderId(id)) {
            log.warn("Attempted to delete non-existing order with ID: {}", id);
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        placeOrderRepository.deleteById(id);
        log.info("Order deleted with ID: {}", id);

        return "Deleted";
    }
}
