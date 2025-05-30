package com.code.monks.nukkad.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Entity(name = "shopkeeper")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorekeeperEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "store_number")
	private String storeNumber;

	@Column(name = "gst_in")
	private String gstIn;

	@Column(name = "address")
	private String address;

	@Column(name = "city")
	private String City;

	@ElementCollection
	@CollectionTable(name = "shopkeeper_pictures", joinColumns = @JoinColumn(name = "shopkeeper_id"))
	@Column(name = "picture_url")
	private List<String> pictures;

}
