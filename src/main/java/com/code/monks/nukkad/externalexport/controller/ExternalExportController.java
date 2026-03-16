package com.code.monks.nukkad.externalexport.controller;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.externalexport.service.ExternalDataFetchService;
import com.code.monks.nukkad.externalexport.service.JsonFileExportService;
import com.code.monks.nukkad.externalexport.service.JsonTransformService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlConstants.ADMIN.BASE)
@RequiredArgsConstructor
public class ExternalExportController {

    private final ExternalDataFetchService fetchService;
    private final JsonTransformService transformService;
    private final JsonFileExportService exportService;

    @PostMapping("/export-json")
    public ResponseEntity<String> exportJson() {

        var rawData = fetchService.fetchAll();
        var transformed = transformService.transform(rawData);

        exportService.export(transformed);

        return ResponseEntity.ok("JSON file generated successfully.");
    }
}

