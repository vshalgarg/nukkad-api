package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.response.DeleteQrCodeResponseDto;
import com.code.monks.nukkad.dto.response.StorekeeperQrCodeResponseDTO;
import com.code.monks.nukkad.dto.response.UpdateStorekeeperQrResponseDTO;
import com.code.monks.nukkad.dto.response.UploadQrCodeResponseDto;
import com.code.monks.nukkad.services.StorekeeperQrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(UrlConstants.STOREKEEPER_QR_CODE.BASE)
@RequiredArgsConstructor
public class StorekeeperQrCodeController {
    private final StorekeeperQrCodeService qrService;

    @PostMapping(UrlConstants.STOREKEEPER_QR_CODE.UPLOAD)
    public ResponseEntity<UploadQrCodeResponseDto> upload(@RequestPart MultipartFile[] qrCodes) {
        log.info("[QR UPLOAD] Received request to upload {} QR code(s)", qrCodes.length);

        UploadQrCodeResponseDto response = qrService.uploadQrCodes(qrCodes);

        log.info("[QR UPLOAD] {}", response.getMessage());
        return ResponseEntity.ok(response);
    }

    @GetMapping(UrlConstants.STOREKEEPER_QR_CODE.GET)
    public ResponseEntity<List<StorekeeperQrCodeResponseDTO>> getAll() {
        log.info("[QR FETCH] Request received to fetch QR codes for current storekeeper");
        List<StorekeeperQrCodeResponseDTO> response = qrService.getAllQrCodes();
        return ResponseEntity.ok(response);
    }

    @PutMapping(UrlConstants.STOREKEEPER_QR_CODE.UPDATE)
    public ResponseEntity<UpdateStorekeeperQrResponseDTO> update(
            @PathVariable Long id,
            @RequestPart MultipartFile qrImage) {

        log.info("[QR UPDATE] Request to update QR code ID={}", id);
        UpdateStorekeeperQrResponseDTO response = qrService.updateQr(id, qrImage);
        log.info("[QR UPDATE] {}", response);

        return ResponseEntity.ok(response);
    }


    @PutMapping(UrlConstants.STOREKEEPER_QR_CODE.MARK_AS_DEFAULT)
    public ResponseEntity<String> markDefault(@PathVariable Long id) {
        qrService.markAsDefault(id);
        return ResponseEntity.ok("Marked as default");
    }

    @DeleteMapping(UrlConstants.STOREKEEPER_QR_CODE.DELETE)
    public ResponseEntity<DeleteQrCodeResponseDto> delete(@PathVariable Long id) {
        log.info("[QR DELETE] Request to delete QR code ID={}", id);

        DeleteQrCodeResponseDto response = qrService.deleteQr(id);

        log.info("[QR DELETE] {}", response.getMessage());
        return ResponseEntity.ok(response);
    }


}
