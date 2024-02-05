package io.pivotal.service.product;

import org.springframework.stereotype.Component;

@Component
public class ProductUpdater {
    public void updateProductEntity(ProductEntity productEntity, Product product) {
        productEntity.setSku(product.getSku());
        productEntity.setName(product.getName());
        productEntity.setDescription(product.getDescription());
        productEntity.setPrice(product.getPrice());
        productEntity.setQuantity(product.getQuantity());
    }
}
