package com.jmarqb.productsapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {

    private String uid;
    private String name;
    private String description;

    private boolean deleted;

    private LocalDateTime deletedAt;

    private List<Product> products;
}
