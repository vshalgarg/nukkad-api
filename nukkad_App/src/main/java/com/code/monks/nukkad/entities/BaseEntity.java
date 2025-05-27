package com.code.monks.nukkad.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@MappedSuperclass
public abstract class BaseEntity {

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	@PreUpdate
	public void onCreateUpdate() {
		LocalDateTime now = LocalDateTime.now();
		if (Objects.isNull(this.createdAt)) {
			this.createdAt = now;
		}
		this.updatedAt = now;
	}

}
