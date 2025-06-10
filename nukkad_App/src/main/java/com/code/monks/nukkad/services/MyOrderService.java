package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.response.MyOrderResponseDTO;
import com.code.monks.nukkad.dto.response.OrderResponseDTO;
import com.code.monks.nukkad.entities.MyOrderEntity;
import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.OrderStatusEnum;
import com.code.monks.nukkad.exception.OrderNotFoundException;
import com.code.monks.nukkad.repositories.MyOrderRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyOrderService
{
    @Autowired
     private MyOrderRespository myOrderRespository;

    public MyOrderResponseDTO createMyOrder(MyOrderEntity myOrderEntity) {
        try {
            MyOrderEntity savedOrder = myOrderRespository.save(myOrderEntity);
            return MyOrderResponseDTO.toResponseDTO(savedOrder);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create MyOrder", e);
        }
    }

    public List<MyOrderResponseDTO> filterOrders(Integer customerId,Integer storeKeeperId)
     {
         List<MyOrderEntity> myOrderEntities;

         if(customerId!= null && storeKeeperId!=null)
         {
             myOrderEntities = myOrderRespository.findByCustomerIdAndStoreKeeperId(customerId,storeKeeperId);
         }
         else if (customerId!=null)
         {
           myOrderEntities=myOrderRespository.findByCustomerId(customerId);
         }
         else if (storeKeeperId!=null)
         {
          myOrderEntities=myOrderRespository.findByStoreKeeperId(storeKeeperId);
         }
         else
         {
             myOrderEntities = myOrderRespository.findAll();
         }
         return myOrderEntities.stream()
                 .map(MyOrderResponseDTO::toResponseDTO)
                 .collect(Collectors.toList());
     }
//     public MyOrderResponseDTO rejectOrder(int myOrderId)
//     {
//          MyOrderEntity myOrderEntity =myOrderRespository.findById(myOrderId)
//           .orElseThrow(()-> new OrderNotFoundException("Order not found with id:"+ myOrderId));
//         myOrderEntity.setOrderStatusEnum(OrderStatusEnum.CANCELLED);
//         myOrderRespository.save(myOrderEntity);

//         return MyOrderResponseDTO.fromEntity(myOrderId);
//     }
}
