package com.myeshop.ProductCommandService.controller;

import com.myeshop.ProductCommandService.command.CreateCategoryCommand;
import com.myeshop.ProductCommandService.rest.CreateCategoryRest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/createCategory") // http://localhost:8082/product-command-service/createCategory
public class CategoryController {

    private final Environment env;
    private final CommandGateway commandGateway;

    @Autowired
    public CategoryController(Environment env, CommandGateway commandGateway) {
        this.env = env;
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createCategory(@RequestBody CreateCategoryRest createCategoryRest) {
        CreateCategoryCommand createCategoryCommand = CreateCategoryCommand.builder()
                .categoryId(UUID.randomUUID().toString())
                .name(createCategoryRest.getName())
                .build();

        String returnValue;

        try{
            returnValue = commandGateway.sendAndWait(createCategoryCommand);
        } catch (Exception ex) {
            returnValue = ex.getLocalizedMessage();
        }

        return returnValue;
    }
}
