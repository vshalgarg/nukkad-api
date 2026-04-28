package com.code.monks.nukkad.externalexport.service;

import com.code.monks.nukkad.externalexport.client.ExternalCategoryFeignClient;
import com.code.monks.nukkad.externalexport.client.ExternalProductFeignClient;
import com.code.monks.nukkad.externalexport.dto.ExternalCategory;
import com.code.monks.nukkad.externalexport.dto.ExternalProduct;
import com.code.monks.nukkad.externalexport.dto.ExternalProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExternalDataFetchService {

    private final ExternalCategoryFeignClient categoryClient;
    private final ExternalProductFeignClient productClient;
    private static final String IMAGE_BASE_URL = "https://image.aapkabazar.co/product/";
    private static final String CATEGORY_IMAGE_BASE_URL = "https://image.aapkabazar.co/cat/";

    public Map<String, Map<String, Object>> fetchAll() {

       // Map<String, List<ExternalProduct>> data = new HashMap<>();
        Map<String, Map<String, Object>> data = new HashMap<>();

        var categoryResponse = categoryClient.getRootCategories("619f219d26d9ad0f34102dd2");

        for (ExternalCategory category : categoryResponse.getCategory()) {

            List<ExternalProduct> allProducts = new ArrayList<>();

            String categoryImage = null;

            if (category.getImages() != null && !category.getImages().isEmpty()) {
                categoryImage =
                        CATEGORY_IMAGE_BASE_URL +
                                category.getId() + "/" +
                                category.getImages().get(0) +
                                "?type=png";
            }
            int page = 1;
            int limit = 50;

            while (true) {

                Map<String, Integer> sort = new HashMap<>();
                sort.put("searchingNumber", -1);

                ExternalProductRequest request =
                        new ExternalProductRequest(
                                category.getId(),
                                "",
                                0,
                                10000,
                                0,
                                100,
                                page,
                                sort,
                                limit
                        );


                var response = productClient.getProducts(request);

                if (response.getProducts() == null || response.getProducts().isEmpty())
                    break;

                // IMAGE URL CONVERSION
                for (ExternalProduct product : response.getProducts()) {

                    if (product.getImages() != null && !product.getImages().isEmpty()) {

                        List<String> fullUrls = new ArrayList<>();

                        for (String img : product.getImages()) {

                            String fullUrl =
                                    IMAGE_BASE_URL
                                            + product.getId()
                                            + "/"
                                            + img
                                            + "?type=png";

                            fullUrls.add(fullUrl);
                        }

                        product.setImages(fullUrls);
                    }
                }

                allProducts.addAll(response.getProducts());

                if (response.getProducts().size() < limit)
                    break;

                page++;
            }

            if (!allProducts.isEmpty()) {

                Map<String, Object> categoryData = new HashMap<>();
                categoryData.put("image", categoryImage);
                categoryData.put("products", allProducts);

                data.put(category.getName(), categoryData);
            }
        }

        return data;
    }
}

