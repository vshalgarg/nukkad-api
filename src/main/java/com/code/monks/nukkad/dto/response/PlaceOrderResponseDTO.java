package com.code.monks.nukkad.dto.response;

<<<<<<< Updated upstream:src/main/java/com/code/monks/nukkad/dto/response/PlaceOrderResponseDTO.java
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceOrderResponseDTO
{
    private String message;
=======
import com.code.monks.nukkad.entities.PlaceOrderEntity;
import com.code.monks.nukkad.enums.PlaceOrderEnum;
import lombok.Data;

@Data
public class PlaceOrderResponseDTO
{
    private Long id;
    private Long orderId;
    private Long itemId;
    private int quantity;
    private PlaceOrderEnum placeOrderEnum;


    public static PlaceOrderResponseDTO fromDbToDto(PlaceOrderEntity placeOrderEntity){
        PlaceOrderResponseDTO responseDTO = new PlaceOrderResponseDTO();
        responseDTO.setId(placeOrderEntity.getId());
        responseDTO.setOrderId(placeOrderEntity.getOrderId());
        responseDTO.setItemId(placeOrderEntity.getItemId());
        responseDTO.setQuantity(placeOrderEntity.getQuantity());
        responseDTO.setPlaceOrderEnum(placeOrderEntity.getPlaceOrderEnum());
        return responseDTO;
    }
>>>>>>> Stashed changes:nukkad_App/src/main/java/com/code/monks/nukkad/dto/response/PlaceOrderResponseDTO.java
}
