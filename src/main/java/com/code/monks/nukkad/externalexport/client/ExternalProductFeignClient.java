package com.code.monks.nukkad.externalexport.client;

import com.code.monks.nukkad.externalexport.dto.ExternalProductRequest;
import com.code.monks.nukkad.externalexport.dto.ExternalProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "externalProductClient",
        url = "https://devfrontendapi.aapkabazar.co"
)
public interface ExternalProductFeignClient {

    @PostMapping("/api/getSmartListProducts")
    ExternalProductResponse getProducts(
            @RequestBody ExternalProductRequest request
    );
}

