package com.code.monks.nukkad.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateStorekeeperQrRequestDTO {

    private MultipartFile qrImage;
}
