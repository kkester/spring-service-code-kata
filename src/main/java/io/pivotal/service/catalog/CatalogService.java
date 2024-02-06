package io.pivotal.service.catalog;

import io.pivotal.service.product.Product;
import io.pivotal.service.product.ProductEntity;
import io.pivotal.service.product.ProductMapper;
import io.pivotal.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        CatalogEntity catalogEntity = catalogRepository.findByCode(catalogCode).orElseThrow(ResourceNotFoundException::new);
        productService.saveProduct(catalogEntity, product);
        Catalog catalog = catalogMapper.toCatalog(catalogEntity);
        catalog.setProducts(productService.getProductsForCatalog(catalogEntity.getId()));
        return catalog;
    }

    public Catalog replaceProducts(String catalogCode, List<Product> products) {
        CatalogEntity catalogEntity = catalogRepository.findByCode(catalogCode)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found"));
        Map<String, Product> productMap = productService.getProductsForCatalog(catalogEntity.getId()).stream()
            .collect(Collectors.toMap(Product::getSku, Function.identity()));
        products.forEach(product -> {
            productMap.remove(product.getSku());
            productService.saveProduct(catalogEntity, product);
        });
        productService.deleteAllProductsFor(catalogEntity.getId(), productMap.keySet());
        return catalogMapper.toCatalog(catalogEntity).toBuilder()
            .products(products)
            .build();
    }
}
