package com.myeshop.ProductCommandService.controller;

import com.myeshop.ProductCommandService.command.CreateProductCommand;
import com.myeshop.ProductCommandService.rest.CreateProductListRest;
import com.myeshop.ProductCommandService.rest.CreateProductRest;
import com.myeshop.ProductCommandService.rest.CreateProductRestWithoutCategoryId;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product") // http://localhost:8082/product-command-service/product
public class ProductController {

    private final Environment env;
    private final CommandGateway commandGateway;

    @Autowired
    public ProductController(Environment env, CommandGateway commandGateway) {
        this.env = env;
        this.commandGateway = commandGateway;
    }

    @PostMapping("/createProduct")
    public String createProduct(@RequestBody CreateProductRest createProductRest) {
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .sku(createProductRest.getSku())
                .name(createProductRest.getName())
                .description(createProductRest.getDescription())
                .active(true)
                .unitPrice(createProductRest.getUnitPrice())
                .imageUrl(createProductRest.getImageUrl())
                .quantity(createProductRest.getQuantity())
                .dateCreated(new Date())
                .lastUpdated(new Date())
                .categoryId(createProductRest.getCategoryId())
                .build();

        String returnValue;

        try{
            returnValue = commandGateway.sendAndWait(createProductCommand);
        } catch (Exception ex) {
            returnValue = ex.getLocalizedMessage();
        }

        return returnValue;
    }

    @PostMapping("/createProductList")
    public String createProductList(@RequestBody CreateProductListRest createProductListRest) {

        String categoryId = createProductListRest.getId();
        List<CreateProductRestWithoutCategoryId> productList = createProductListRest.getProducts();
        if (productList == null) return "Failure!";
        for (CreateProductRestWithoutCategoryId product: productList) {
            CreateProductCommand createProductCommand = CreateProductCommand.builder()
                    .productId(UUID.randomUUID().toString())
                    .sku(product.getSku())
                    .name(product.getName())
                    .description(product.getDescription())
                    .active(true)
                    .unitPrice(product.getUnitPrice())
                    .imageUrl(product.getImageUrl())
                    .quantity(product.getQuantity())
                    .dateCreated(new Date())
                    .lastUpdated(new Date())
                    .categoryId(categoryId)
                    .build();

            String returnValue;

            try{
                returnValue = commandGateway.sendAndWait(createProductCommand);
            } catch (Exception ex) {
                returnValue = ex.getLocalizedMessage();
                return returnValue;
            }
        }

        return "Successfully created productList for '" + createProductListRest.getCategoryName()+ "'!";
    }

    @GetMapping
    public String getProduct() {
        return "HTTP GET Handled " + env.getProperty("local.server.port");
    }

}
