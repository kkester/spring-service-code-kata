package io.pivotal.service.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String sku;
    private String name;
    private String price;
    private String description;
    private int quantity;
}
