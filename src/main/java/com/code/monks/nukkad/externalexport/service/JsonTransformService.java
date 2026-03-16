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

    public ExportRoot transform(Map<String, List<ExternalProduct>> rawData) {

        List<ExportCategory> categories = new ArrayList<>();

        for (Map.Entry<String, List<ExternalProduct>> entry : rawData.entrySet()) {

            String categoryName = capitalize(entry.getKey());
            List<ExportProduct> products = new ArrayList<>();

            Set<String> unique = new HashSet<>();

            for (ExternalProduct product : entry.getValue()) {

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

            categories.add(new ExportCategory(categoryName, products));
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

