package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.AddressEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @NotBlank(message = "Address Line 1 is required")
    @Size(max = 100, message = "Address Line 1 must be at most 100 characters")
    private String addressLine1;

    @Size(max = 100, message = "Address Line 2 must be at most 100 characters")
    private String addressLine2;

    @Size(max = 100, message = "Landmark must be at most 100 characters")
    private String landmark;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must be at most 50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 50, message = "State must be at most 50 characters")
    private String state;

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Pincode must be a 6-digit number starting from 1-9")
    private String pincode;

    public static AddressEntity toEntity(CreateAddressRequestDTO dto) {
        AddressEntity address = new AddressEntity();
        address.setName(dto.getName());
        address.setMobileNumber(dto.getMobileNumber());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setLandmark(dto.getLandmark());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        return address;
    }
}
