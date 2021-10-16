package com.myeshop.ProductCommandService.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
public class CreateProductCommand {

    @TargetAggregateIdentifier
    private final String productId;
    private final String sku;
    private final String name;
    private final String description;
    private final Boolean active;
    private final BigDecimal unitPrice;
    private final String imageUrl;
    private final int quantity;
    private final Date dateCreated;
    private final Date lastUpdated;
    private final String categoryId;
}
