package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.converter.UnitEnumConverter;
import com.code.monks.nukkad.enums.UnitEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Entity(name = "item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name  = "unit")
	private String Unit;

	@Column(name = "quantity")
	private int quantity;

	@OneToMany( mappedBy = "item", cascade = CascadeType.ALL)
	private List<ImageEntity> images = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "item_category", joinColumns = @JoinColumn(name = "item_id"),
			inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<CategoryEntity> categories = new ArrayList<>();
}
