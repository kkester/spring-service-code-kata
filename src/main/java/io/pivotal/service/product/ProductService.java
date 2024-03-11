package io.pivotal.service.product;

import io.pivotal.service.catalog.CatalogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void replaceProducts(CatalogEntity catalogEntity, List<Product> products) {

        List<String> skus = new ArrayList<>();
        products.forEach(product -> {
            skus.add(product.getSku());
            saveProduct(catalogEntity, product);
        });

        UUID catalogEntityId = catalogEntity.getId();
        if (products.isEmpty()) {
            productRepository.deleteAllByCatalogId(catalogEntityId);
        } else {
            productRepository.deleteAllByCatalogIdAndSkuIsNotIn(catalogEntityId, skus);
        }
    }

    public void saveProduct(CatalogEntity catalogEntity, Product product) {
        Optional<ProductEntity> productEntityOptional = productRepository.findByCatalogIdAndSku(catalogEntity.getId(), product.getSku());
        productEntityOptional.ifPresentOrElse(productEntity -> {
            productUpdater.updateProductEntity(productEntity, product);
            productRepository.save(productEntity);
        }, () -> {
            ProductEntity productEntity = productMapper.toProductEntity(product);
            productEntity.setCatalog(catalogEntity);
            productRepository.save(productEntity);
        });
    }
}
