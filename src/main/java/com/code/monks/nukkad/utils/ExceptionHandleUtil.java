package com.code.monks.nukkad.utils;

import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExceptionHandleUtil {

    private final StorekeeperRepository storekeeperRepository;
    private final CustomerRepository customerRepository;

    public void validateStorekeeperUniqueFields(StorekeeperEntity entity) {
        List<String> conflictingFields = new ArrayList<>();

        Optional<StorekeeperEntity> gstConflict = storekeeperRepository.findByGstNum(entity.getGstNum());
        if (gstConflict.isPresent() && !gstConflict.get().getId().equals(entity.getId())) {
            conflictingFields.add("GST number");
        }

        Optional<StorekeeperEntity> addressConflict = storekeeperRepository.findByAddressLine1(entity.getAddressLine1());
        if (addressConflict.isPresent() && !addressConflict.get().getId().equals(entity.getId())) {
            conflictingFields.add("address line 1");
        }

        if (!conflictingFields.isEmpty()) {
            String message = formatConflictMessage(conflictingFields);
            throw new DuplicateResourceException(ResponseErrorCodes.DUPLICATE_RESOURCE_EXCEPTION, message);
        }
    }

    public void validateCustomerUniqueFields(CustomerEntity entity) {
        List<String> conflictingFields = new ArrayList<>();

        Optional<CustomerEntity> emailConflict = customerRepository.findByEmail(entity.getEmail());
        if (emailConflict.isPresent() && !emailConflict.get().getId().equals(entity.getId())) {
            conflictingFields.add("email");
        }

        Optional<CustomerEntity> mobileConflict = customerRepository.findByMobileNumber(entity.getMobileNumber());
        if (mobileConflict.isPresent() && !mobileConflict.get().getId().equals(entity.getId())) {
            conflictingFields.add("mobile number");
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
