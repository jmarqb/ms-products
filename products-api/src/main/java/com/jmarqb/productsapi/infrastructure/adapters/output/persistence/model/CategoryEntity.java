package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_uid", columnList = "uid"),
        @Index(name = "idx_deleted", columnList = "deleted")})
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
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products;
}
