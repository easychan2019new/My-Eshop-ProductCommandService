package com.myeshop.ProductCommandService.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
public class CreateCategoryCommand {

    @TargetAggregateIdentifier
    private final String categoryId;
    private final String name;
}
