package com.myeshop.ProductCommandService.rest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRest {
    String sku;
    String name;
    String description;
    BigDecimal unitPrice;
    String imageUrl;
    int quantity;
    String categoryId;
}
