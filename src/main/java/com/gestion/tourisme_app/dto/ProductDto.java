package com.gestion.tourisme_app.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private double price;
    private double discountedPrice;
    private String imageUrl;
    private List<String> colors;
    private List<String> sizes;
    private Integer stockQuantity;
    private Boolean isActive;
    private Boolean isFeatured;
    private double rating;
    private Integer reviewCount;
    private LocalDateTime createdDate;
    private CategoryDto category;
    private boolean isFavorite;
}