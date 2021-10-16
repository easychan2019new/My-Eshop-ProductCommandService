package com.myeshop.ProductCommandService.rest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRestWithoutCategoryId {
    String sku;
    String name;
    String description;
    BigDecimal unitPrice;
    String imageUrl;
    int quantity;
}
