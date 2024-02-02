package io.pivotal.service.catalog;

import io.pivotal.service.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Catalog {
    private String code;
    private String displayName;
    private List<Product> products;
}
