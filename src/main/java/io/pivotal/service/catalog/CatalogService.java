package io.pivotal.service.catalog;

import io.pivotal.service.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository catalogRepository;

    public List<Catalog> getAll() {
        return null;
    }

    public Catalog addProduct(Product product) {
        return null;
    }

    public Catalog replaceProducts(List<Product> product) {
        return null;
    }
}
