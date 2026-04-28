package com.code.monks.nukkad.externalexport.service;

import com.code.monks.nukkad.externalexport.dto.ExternalProduct;
import com.code.monks.nukkad.externalexport.dto.json.ExportCategory;
import com.code.monks.nukkad.externalexport.dto.json.ExportProduct;
import com.code.monks.nukkad.externalexport.dto.json.ExportRoot;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JsonTransformService {

    public ExportRoot transform(Map<String, Map<String, Object>> rawData) {

        List<ExportCategory> categories = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> entry : rawData.entrySet()) {

            String categoryName = capitalize(entry.getKey());

            String categoryImage = (String) entry.getValue().get("image");

            List<ExternalProduct> productList =
                    (List<ExternalProduct>) entry.getValue().get("products");

            List<ExportProduct> products = new ArrayList<>();
            Set<String> unique = new HashSet<>();

            for (ExternalProduct product : productList) {

                if (unique.contains(product.getName()))
                    continue;

                unique.add(product.getName());

                String unit = detectUnit(product.getRecommendedAttribute());

                List<String> images;

                if (product.getImages() != null && !product.getImages().isEmpty()) {
                    images = List.of(product.getImages().get(0));
                } else {
                    images = List.of("images/default.jpg");
                }

                products.add(new ExportProduct(
                        product.getName(),
                        unit,
                        images
                ));
            }

            // CATEGORY WITH IMAGE
            categories.add(new ExportCategory(
                    categoryName,
                    categoryImage != null ? categoryImage : "images/default-category.jpg",
                    products
            ));
        }

        return new ExportRoot(categories);
    }

    private String detectUnit(String attr) {
        if (attr == null) return "PIECE";

        String lower = attr.toLowerCase();

        if (lower.contains("kg")) return "KG";
        if (lower.contains("gm")) return "KG";
        if (lower.contains("litre") || lower.contains("ml")) return "LITER";

        return "PIECE";
    }

    private String capitalize(String input) {
        return Arrays.stream(input.split(" "))
                .map(word -> word.substring(0,1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}