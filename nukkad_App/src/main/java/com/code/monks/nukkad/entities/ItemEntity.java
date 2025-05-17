package com.code.monks.nukkad.entities;

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
	private int id;

	private String name;

	private String image;

	private String unit;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "item_category", joinColumns = @JoinColumn(name = "item_id"),
			inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<CategoryEntity> categories = new ArrayList<>();
}
