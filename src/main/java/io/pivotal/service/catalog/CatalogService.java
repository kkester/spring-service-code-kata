package io.pivotal.service.catalog;

import io.pivotal.service.product.Product;
import io.pivotal.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository catalogRepository;
    private final ProductService productService;
    private final CatalogMapper catalogMapper;

    public List<Catalog> getAll() {
        return null;
    }

    public Catalog addProduct(String catalogCode, Product product) {
        return null;
    }

    public Catalog replaceProducts(String catalogCode, List<Product> products) {
        return null;
    }
}
