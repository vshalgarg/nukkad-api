 package com.code.monks.nukkad.entities;

 import jakarta.persistence.*;
 import lombok.AllArgsConstructor;
 import lombok.Data;
 import lombok.EqualsAndHashCode;
 import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AddressEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "is_default",nullable = false)
    private Boolean isDefault = false;

    // Helper method to build full address string with all fields
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();

        if (name != null && !name.isBlank()) sb.append(name);
        if (mobileNumber != null && !mobileNumber.isBlank()) sb.append(", ").append(mobileNumber);
        if (addressLine1 != null && !addressLine1.isBlank()) sb.append(", ").append(addressLine1);
        if (addressLine2 != null && !addressLine2.isBlank()) sb.append(", ").append(addressLine2);
        if (landmark != null && !landmark.isBlank()) sb.append(", Landmark: ").append(landmark);
        if (city != null && !city.isBlank()) sb.append(", ").append(city);
        if (state != null && !state.isBlank()) sb.append(", ").append(state);
        if (pincode != null && !pincode.isBlank()) sb.append(" - ").append(pincode);

        return sb.toString();
    }
}
