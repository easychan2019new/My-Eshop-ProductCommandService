package com.myeshop.ProductCommandService.aggregate;

import com.myeshop.Core.Product.command.ReserveProductCommand;
import com.myeshop.Core.Product.command.RollbackProductCommand;
import com.myeshop.Core.Product.event.ProductCreatedEvent;
import com.myeshop.Core.Product.event.ProductReservedEvent;
import com.myeshop.Core.Product.event.ProductRollbackEvent;
import com.myeshop.ProductCommandService.command.CreateProductCommand;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

@Aggregate(snapshotTriggerDefinition = "productSnapshotTriggerDefinition")
@NoArgsConstructor
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String sku;
    private String name;
    private String description;
    private Boolean active;
    private BigDecimal unitPrice;
    private String imageUrl;
    private int quantity;
    private Date dateCreated;
    private Date lastUpdated;
    private String categoryId;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductAggregate.class);

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
        LOGGER.info("Publish productCreatedEvent!");
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent){
        this.productId = productCreatedEvent.getProductId();
        this.sku = productCreatedEvent.getSku();
        this.name = productCreatedEvent.getName();
        this.description = productCreatedEvent.getDescription();
        this.active = productCreatedEvent.getActive();
        this.unitPrice = productCreatedEvent.getUnitPrice();
        this.imageUrl = productCreatedEvent.getImageUrl();
        this.quantity = productCreatedEvent.getQuantity();
        this.dateCreated = productCreatedEvent.getDateCreated();
        this.lastUpdated = productCreatedEvent.getLastUpdated();
        this.categoryId = productCreatedEvent.getCategoryId();

        LOGGER.info("Persist productCreatedEvent!");
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand) {
        LOGGER.info("Inside Method ReserveProductCommand");
        if (quantity < reserveProductCommand.getQuantity()) {
            throw new IllegalArgumentException("Insufficient number of items in stock");
        }

        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .orderId(reserveProductCommand.getOrderId())
                .build();

        AggregateLifecycle.apply(productReservedEvent);
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        this.quantity -= productReservedEvent.getQuantity();
    }

    @CommandHandler
    public void handle(RollbackProductCommand rollbackProductCommand) {
        LOGGER.info("Inside Method RollbackProductCommand");

        ProductRollbackEvent productRollbackEvent = ProductRollbackEvent.builder()
                .productId(rollbackProductCommand.getProductId())
                .quantity(rollbackProductCommand.getQuantity())
                .orderId(rollbackProductCommand.getOrderId())
                .build();

        AggregateLifecycle.apply(productRollbackEvent);
    }

    @EventSourcingHandler
    public void on(ProductRollbackEvent productRollbackEvent) {
        this.quantity += productRollbackEvent.getQuantity();
    }

}
