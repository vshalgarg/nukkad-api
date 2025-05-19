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
public class ShopkeeperEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String storeNumber;

	private String gstIn;

	private String address;

	private String City;

	@ElementCollection
	@CollectionTable(name = "shopkeeper_pictures", joinColumns = @JoinColumn(name = "shopkeeper_id"))
	@Column(name = "picture_url")
	private List<String> pictures;

}
