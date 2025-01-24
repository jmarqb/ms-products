package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories", indexes = {
	@Index(name = "idx_cat_name", columnList = "name"),
	@Index(name = "idx_cat_uid", columnList = "uid"),
	@Index(name = "idx_cat_deleted", columnList = "deleted")})
public class CategoryEntity {

	@Id
	@Column(name = "uid")
	private String uid;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "deleted", columnDefinition = "boolean default false")
	private boolean deleted;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@ToString.Exclude
	@OneToMany(mappedBy = "category")
	private List<ProductEntity> products;
}
