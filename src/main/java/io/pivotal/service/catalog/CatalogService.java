package io.pivotal.service.catalog;

import io.pivotal.service.product.Product;
import io.pivotal.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository catalogRepository;
    private final ProductService productService;
    private final CatalogMapper catalogMapper;

    public List<Catalog> getAll() {
        return catalogRepository.findAll().stream()
            .map(catalogMapper::toCatalog)
            .toList();
    }

    public Catalog addProduct(String catalogCode, Product product) {
        CatalogEntity catalogEntity = catalogRepository.findByCode(catalogCode)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found"));
        productService.saveProduct(catalogEntity, product);
        Catalog catalog = catalogMapper.toCatalog(catalogEntity);
        catalog.setProducts(productService.getProductsForCatalog(catalogEntity.getId()));
        return catalog;
    }

    public Catalog replaceProducts(String catalogCode, List<Product> products) {
        CatalogEntity catalogEntity = catalogRepository.findByCode(catalogCode)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found"));

        List<String> skus = new ArrayList<>();
        products.forEach(product -> {
            skus.add(product.getSku());
            productService.saveProduct(catalogEntity, product);
        });
        productService.removeAllProductsFromCatalogExcept(catalogEntity.getId(), skus);

        return catalogMapper.toCatalog(catalogEntity).toBuilder()
            .products(productService.getProductsForCatalog(catalogEntity.getId()))
            .build();
    }
}
