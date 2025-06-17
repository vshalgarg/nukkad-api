package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "storekeeper")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorekeeperEntity extends UserEntity{
	@Id
	@Column(name = "id")
    private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "store_name")
	private String storeName;

	@Column(name = "contact_number")
	private String contactNumber;

	@Column(name = "gst_in")
	private String gstIn;

	@Column(name = "address_line1")
	private String addressLine1;

	@Column(name = "address_line2")
	private String addressLine2;

	@Column(name = "landmark")
	private String landmark;

	@Column(name = "city")
	private String City;

	@Column(name = "state")
	private String state;

	@Column(name = "pincode")
	private String pincode;

	@Column(name = "store_id", unique = true, nullable = false, updatable = false)
	private String storeId;

	@Override
	public RoleEnum getRole(){
		return RoleEnum.STOREKEEPER;
	}

}
