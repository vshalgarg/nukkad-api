package com.code.monks.nukkad.externalexport.client;

import com.code.monks.nukkad.externalexport.dto.ExternalCategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "externalCategoryClient",
        url = "https://devfrontendapi.aapkabazar.co"
)
public interface ExternalCategoryFeignClient {

    @GetMapping("/api/root/category")
    ExternalCategoryResponse getRootCategories(
            @RequestParam("cityId") String cityId
    );
}


