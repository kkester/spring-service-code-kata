package io.pivotal.service.product;

public class ProductFactory {
    private ProductFactory() {
    }

    public static ProductEntity createProductEntity() {
        return ProductEntity.builder()
            .sku("abc")
            .name("Ball")
            .description("Round Ball")
            .price("$0.99")
            .quantity(10)
            .build();
    }

    public static Product createProduct() {
        return Product.builder()
            .catalogCode("xyz")
            .sku("abc")
            .name("Ball")
            .description("Round Ball")
            .price("$0.99")
            .quantity(10)
            .build();
    }
}
