package com.code.monks.nukkad.services;

import com.code.monks.nukkad.admin.request.AdminLoginRequestDto;
import com.code.monks.nukkad.admin.request.AdminRegisterRequestDto;
import com.code.monks.nukkad.admin.response.AdminLoginResponseDto;
import com.code.monks.nukkad.admin.response.AdminRegisterResponseDto;
import com.code.monks.nukkad.dto.jsonUpload.Categories;
import com.code.monks.nukkad.dto.jsonUpload.ImportJsonDataResponse;
import com.code.monks.nukkad.dto.request.ItemExcelDTO;
import com.code.monks.nukkad.dto.response.DeleteCustomerResponseDTO;
import com.code.monks.nukkad.dto.response.DeleteStorekeeperResponseDTO;
import com.code.monks.nukkad.dto.response.GetImageSyncStatusResponse;
import com.code.monks.nukkad.dto.response.UploadExcelFileResponseDto;
import com.code.monks.nukkad.enums.ImageUploadStatus;

import java.util.List;

public interface AdminService {

    AdminLoginResponseDto login(AdminLoginRequestDto loginRequestDto);
    AdminRegisterResponseDto register(AdminRegisterRequestDto registerRequestDto);
    UploadExcelFileResponseDto createProductsFromExcel(List<ItemExcelDTO> dtos);
    ImportJsonDataResponse importProductsToExistingCategories(Categories categoriesRequest);

    DeleteCustomerResponseDTO deleteCustomerById(Long id);
    DeleteStorekeeperResponseDTO deleteStorekeeperById(Long id);

    GetImageSyncStatusResponse getImageSyncStatus(
            ImageUploadStatus status,
            Long lastSeenId
    );

}
