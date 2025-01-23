package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_uid", columnList = "uid"),
        @Index(name = "idx_price", columnList = "price"),
        @Index(name = "idx_category", columnList = "category_id"),
        @Index(name = "idx_deleted", columnList = "deleted")})
public class ProductEntity {

    @Id
    @Column(name = "uid")
    private String uid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "stock")
    private Long stock;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
