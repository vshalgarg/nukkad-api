package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.converter.UnitEnumToCodeConverter;
import com.code.monks.nukkad.enums.UnitEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "item")
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ItemEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "item_gen")
    @TableGenerator(
            name = "item_gen",
            table = "id_generator",
            pkColumnName = "generator_name",
            valueColumnName = "generator_value",
            pkColumnValue = "item_id",
            allocationSize = 500,
            initialValue  = 1
    )
    @Column(name = "id")
    private Long id;

    @EqualsAndHashCode.Include
	@Column(name = "name",nullable = false, length = 255)
	private String name;

	@Convert(converter = UnitEnumToCodeConverter.class)
	@Column(name  = "unit")
	private UnitEnum unit;

	@OneToMany(mappedBy = "item",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY)
	private List<CategoryItemImageEntity> images = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "item_category", joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<CategoryEntity> categories = new ArrayList<>();

    // HELPER METHOD (VERY IMPORTANT)
    public void addImage(CategoryItemImageEntity image) {
            images.add(image);
        image.setItem(this);

}}
