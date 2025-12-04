package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteStorekeeperResponseDTO {

    private Long storekeeperId;
    private String message;
    private boolean success;
}
