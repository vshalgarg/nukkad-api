package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(onlyExplicitlyIncluded = true ,callSuper = false)
@Entity
@Table(name = "storekeeper")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorekeeperEntity extends UserEntity{
	@EqualsAndHashCode.Include
	@Id
	@Column(name = "id")
    private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "store_name")
	private String storeName;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "contact_number")
	private String contactNumber;

	@Column(name = "gst_in")
	private String gstNum;

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

	@NaturalId
	@Column(name = "store_qr_id", unique = true, nullable = false, updatable = false)
	private String storeQrId;

	@OneToMany(mappedBy = "storekeeper", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StorekeeperImageEntity> images = new ArrayList<>();

	@OneToMany(mappedBy = "storekeeper", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StorekeeperQrCodeEntity> qrCodes = new ArrayList<>();

	@Override
	public RoleEnum getRole(){
		return RoleEnum.STOREKEEPER;
	}

}
