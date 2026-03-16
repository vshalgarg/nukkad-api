package com.code.monks.nukkad.externalexport.service;

import com.code.monks.nukkad.externalexport.dto.json.ExportRoot;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class JsonFileExportService {

    private final ObjectMapper objectMapper;

    public void export(ExportRoot data) {

        try {

            //File file = new File("store-data.json");
            String fileName = "store-data-" +
                    LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                    + ".json";

            File file = new File(fileName);

            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(file, data);

            System.out.println("JSON generated at: " + file.getAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JSON file", e);
        }
    }
}

