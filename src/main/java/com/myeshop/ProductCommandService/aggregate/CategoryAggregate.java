package com.myeshop.ProductCommandService.aggregate;

import com.myeshop.Core.category.event.CategoryCreatedEvent;
import com.myeshop.ProductCommandService.command.CreateCategoryCommand;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Aggregate
@NoArgsConstructor
public class CategoryAggregate {

    @AggregateIdentifier
    private String categoryId;
    private String name;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryAggregate.class);

    @Autowired
    private EventGateway eventGateway;

    @CommandHandler
    public CategoryAggregate(CreateCategoryCommand createCategoryCommand) {
        CategoryCreatedEvent categoryCreatedEvent = new CategoryCreatedEvent();
        BeanUtils.copyProperties(createCategoryCommand, categoryCreatedEvent);
        LOGGER.info("Publish categoryCreatedEvent!");
        AggregateLifecycle.apply(categoryCreatedEvent);
    }

    @EventSourcingHandler
    public void on(CategoryCreatedEvent categoryCreatedEvent){
        LOGGER.info("Persist categoryCreatedEvent!");
        this.categoryId = categoryCreatedEvent.getCategoryId();
        this.name = categoryCreatedEvent.getName();
    }
}
