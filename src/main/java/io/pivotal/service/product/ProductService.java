package io.pivotal.service.product;

import io.pivotal.service.catalog.CatalogEntity;
import io.pivotal.service.errors.ResourceConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductUpdater productUpdater;
    private final ProductMapper productMapper;

    public List<Product> getProductsForCatalog(UUID id) {
        return productRepository.findAllByCatalogId(id).stream()
            .map(productMapper::toProduct)
            .toList();
    }

    public void addProduct(CatalogEntity catalogEntity, Product product) {
        Optional<ProductEntity> productEntityOptional = productRepository.findByCatalogIdAndSku(catalogEntity.getId(), product.getSku());
        productEntityOptional.ifPresentOrElse(
            productEntity -> {
                throw new ResourceConflictException("Product already exists");
            },
            () -> createProduct(catalogEntity, product)
        );
    }

    public void replaceProducts(CatalogEntity catalogEntity, List<Product> products) {
        List<String> skus = products.stream()
            .map(product -> saveProduct(catalogEntity, product).getSku())
            .toList();
        deleteCatalogProductsExcept(catalogEntity, skus);
    }

    private ProductEntity saveProduct(CatalogEntity catalogEntity, Product product) {
        ProductEntity productEntity = productRepository.findByCatalogIdAndSku(catalogEntity.getId(), product.getSku())
            .map(existingProductEntity -> productUpdater.updateProductEntity(existingProductEntity, product))
            .orElseGet(() -> createProduct(catalogEntity, product));
        return productRepository.save(productEntity);
    }

    private ProductEntity createProduct(CatalogEntity catalogEntity, Product product) {
        ProductEntity productEntity = productMapper.toProductEntity(product, catalogEntity);
        return productRepository.save(productEntity);
    }

    private void deleteCatalogProductsExcept(CatalogEntity catalogEntity, List<String> skus) {
        UUID catalogEntityId = catalogEntity.getId();
        if (skus.isEmpty()) {
            productRepository.deleteAllByCatalogId(catalogEntityId);
        } else {
            productRepository.deleteAllByCatalogIdAndSkuIsNotIn(catalogEntityId, skus);
        }
    }
}
