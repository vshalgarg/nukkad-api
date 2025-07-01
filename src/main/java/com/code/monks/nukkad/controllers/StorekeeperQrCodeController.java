package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.response.StorekeeperQrCodeResponseDTO;
import com.code.monks.nukkad.dto.response.UpdateStorekeeperQrResponseDTO;
import com.code.monks.nukkad.entities.StorekeeperQrCodeEntity;
import com.code.monks.nukkad.services.StorekeeperQrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(UrlConstants.STOREKEEPER_QR_CODE.BASE)
@RequiredArgsConstructor
public class StorekeeperQrCodeController {
    private final StorekeeperQrCodeService qrService;

    @PostMapping(UrlConstants.STOREKEEPER_QR_CODE.UPLOAD)
    public ResponseEntity<String> upload(@RequestPart MultipartFile[] qrCodes) {
        qrService.uploadQrCodes(qrCodes);
        return ResponseEntity.ok("Uploaded");
    }

    @GetMapping(UrlConstants.STOREKEEPER_QR_CODE.GET)
    public ResponseEntity<List<StorekeeperQrCodeResponseDTO>> getAll() {
        List<StorekeeperQrCodeEntity> list = qrService.getAllQrCodes();
        return ResponseEntity.ok(list.stream()
                .map(StorekeeperQrCodeResponseDTO::fromEntity)
                .toList());
    }

    @PutMapping(UrlConstants.STOREKEEPER_QR_CODE.UPDATE)
    public ResponseEntity<UpdateStorekeeperQrResponseDTO> update(
            @PathVariable Long id,
            @RequestPart MultipartFile qrImage) {
        var updated = qrService.updateQr(id, qrImage);
        return ResponseEntity.ok(UpdateStorekeeperQrResponseDTO.fromEntity(updated));
    }

    @PutMapping(UrlConstants.STOREKEEPER_QR_CODE.MARK_AS_DEFAULT)
    public ResponseEntity<String> markDefault(@PathVariable Long id) {
        qrService.markAsDefault(id);
        return ResponseEntity.ok("Marked as default");
    }

    @DeleteMapping(UrlConstants.STOREKEEPER_QR_CODE.DELETE)
    public ResponseEntity<String> delete(@PathVariable Long id) {
        qrService.deleteQr(id);
        return ResponseEntity.ok("Deleted");
    }
}
