package com.myeshop.ProductCommandService.rest;

import lombok.Data;

import java.util.List;

@Data
public class CreateProductListRest {
    String id;
    String categoryName;
    List<CreateProductRestWithoutCategoryId> products;
}
