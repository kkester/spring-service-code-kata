package io.pivotal.service.product;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toProduct(ProductEntity productEntity) {
        return Product.builder()
            .sku(productEntity.getSku())
            .name(productEntity.getName())
            .description(productEntity.getDescription())
            .price(productEntity.getPrice())
            .quantity(productEntity.getQuantity())
            .build();
    }
}
