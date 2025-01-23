package com.jmarqb.productsapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    private String uid;
    private String name;
    private String description;
    private BigDecimal price;

    private Long stock;

    private String categoryId;

    private boolean deleted;

    private LocalDateTime deletedAt;

}
