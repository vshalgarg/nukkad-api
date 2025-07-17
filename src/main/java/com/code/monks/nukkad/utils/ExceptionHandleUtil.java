package com.code.monks.nukkad.utils;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class  ExceptionHandleUtil {

    private final StorekeeperRepository storekeeperRepository;

    public void validateUniqueFields(StorekeeperEntity entity) {
        List<String> conflictingFields = new ArrayList<>();

        if (storekeeperRepository.existsByGstNum(entity.getGstNum())) {
            conflictingFields.add("GST number");
        }
        if (storekeeperRepository.existsByMobileNumber(entity.getMobileNumber())) {
            conflictingFields.add("mobile number");
        }
        if (storekeeperRepository.existsByStoreQrId(entity.getStoreQrId())) {
            conflictingFields.add("store QR ID");
        }
        if (storekeeperRepository.existsByAddressLine1(entity.getAddressLine1())) {
            conflictingFields.add("address line 1");
        }

        if (!conflictingFields.isEmpty()) {
            String message = formatConflictMessage(conflictingFields);
            throw new DuplicateResourceException(ResponseErrorCodes.DUPLICATE_RESOURCE_EXCEPTION, message);
        }
    }

    private String formatConflictMessage(List<String> fields) {
        if (fields.size() == 1) {
            return "This " + fields.get(0) + " already exists.";
        }
        String last = fields.remove(fields.size() - 1);
        String joined = String.join(", ", fields);
        return "These " + joined + " & " + last + " already exist.";
    }
}
