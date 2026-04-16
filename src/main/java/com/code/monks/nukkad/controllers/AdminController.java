package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.admin.request.AdminLoginRequestDto;
import com.code.monks.nukkad.admin.request.AdminRegisterRequestDto;
import com.code.monks.nukkad.admin.response.AdminLoginResponseDto;
import com.code.monks.nukkad.admin.response.AdminRegisterResponseDto;
import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.jsonUpload.Categories;
import com.code.monks.nukkad.dto.jsonUpload.ImportJsonDataResponse;
import com.code.monks.nukkad.dto.request.ItemExcelDTO;
import com.code.monks.nukkad.dto.response.DeleteCustomerResponseDTO;
import com.code.monks.nukkad.dto.response.DeleteStorekeeperResponseDTO;
import com.code.monks.nukkad.dto.response.GetImageSyncStatusResponse;
import com.code.monks.nukkad.dto.response.UploadExcelFileResponseDto;
import com.code.monks.nukkad.enums.ImageUploadStatus;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.services.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.ADMIN.*;

@RestController
@RequestMapping(UrlConstants.ADMIN.BASE)
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @PostMapping(LOGIN)
    public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminLoginRequestDto loginRequestDto) {
        log.info("[ADMIN CONTROLLER] Login endpoint hit for email: {}", loginRequestDto.getEmail());
        AdminLoginResponseDto responseDto = adminService.login(loginRequestDto);
        log.info("[ADMIN CONTROLLER] Login successful for email: {}", loginRequestDto.getEmail());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(REGISTER)
    public ResponseEntity<AdminRegisterResponseDto> register(@RequestBody AdminRegisterRequestDto request) {
        AdminRegisterResponseDto response = adminService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(UPLOAD_EXCEL_FILE)
    public ResponseEntity<UploadExcelFileResponseDto> uploadExcel(@RequestParam("file") MultipartFile file) {
        List<ItemExcelDTO> dtos = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            log.info("Starting to process Excel file with {} rows", sheet.getLastRowNum());

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                ItemExcelDTO dto = new ItemExcelDTO();
                dto.setName(getCellValueAsString(row.getCell(0)));
                dto.setUnit(getCellValueAsString(row.getCell(1)));

                // Categories
                String categoriesCell = getCellValueAsString(row.getCell(2));
                List<Long> categoryIds = new ArrayList<>();
                if (!categoriesCell.isEmpty()) {
                    Arrays.stream(categoriesCell.split(","))
                            .map(String::trim)
                            .map(Long::parseLong)
                            .forEach(categoryIds::add);
                }
                dto.setCategoryIds(categoryIds);

                // Images
                String imagesCell = getCellValueAsString(row.getCell(3));
                List<String> imageUrls = new ArrayList<>();
                if (!imagesCell.isEmpty()) {
                    Arrays.stream(imagesCell.split(","))
                            .map(String::trim)
                            .forEach(imageUrls::add);
                }
                dto.setImageUrls(imageUrls);

                dtos.add(dto);
            }

            log.info("Finished processing Excel file. Total DTOs parsed: {}", dtos.size());

        } catch (Exception e) {
            log.error("Failed to parse Excel file", e);
            throw new UnhandledException(ResponseErrorCodes.FAILED_TO_PARSE_EXCEL_FILE, e);
        }
        UploadExcelFileResponseDto responseDto =  adminService.createProductsFromExcel(dtos);
        return ResponseEntity.ok(responseDto);
    }


    @PostMapping(UPLOAD_JSON_FILE)
    public ResponseEntity<ImportJsonDataResponse> importProductsToExistingCategories(
             @Valid @RequestBody Categories categoriesRequest) {
       return new ResponseEntity<>(adminService.importProductsToExistingCategories(categoriesRequest), HttpStatus.CREATED);
    }

    @GetMapping(IMAGE_SYNC_STATUS)
    public ResponseEntity<GetImageSyncStatusResponse> getImageSyncStatus(
            @RequestParam ImageUploadStatus status,
            @RequestParam(defaultValue = "0") Long lastSeenId) {

        log.info("[ADMIN CONTROLLER] Image sync status requested. "
                + "Status: {}, lastSeenId: {}", status, lastSeenId);

        GetImageSyncStatusResponse response = adminService
                .getImageSyncStatus(status, lastSeenId);//method called

        return ResponseEntity.ok(response);
    }

@DeleteMapping(DELETE_CUSTOMER)
public ResponseEntity<DeleteCustomerResponseDTO> deleteCustomerById(@PathVariable Long id){
    DeleteCustomerResponseDTO response = adminService.deleteCustomerById(id);
    if (response.isSuccess()) {
        return ResponseEntity.ok(response);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
    @DeleteMapping(DELETE_STOREKEEPER)
    public ResponseEntity<DeleteStorekeeperResponseDTO> deleteStorekeeperById(@PathVariable Long id){
        DeleteStorekeeperResponseDTO response = adminService.deleteStorekeeperById(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Utility to fetch string value from a cell safely
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Converting numeric to String without decimals
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
