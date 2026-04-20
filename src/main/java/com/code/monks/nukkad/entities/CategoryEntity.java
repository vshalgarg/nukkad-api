package com.code.monks.nukkad.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "category")
@Getter@Setter
public class CategoryEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;


	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "image_id")
	private CategoryItemImageEntity image;

	@ManyToMany(mappedBy = "categories")
	private List<ItemEntity> items = new ArrayList<>();


}
