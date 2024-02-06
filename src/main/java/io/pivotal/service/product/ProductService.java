package io.pivotal.service.product;

import io.pivotal.service.catalog.CatalogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductUpdater productUpdater;
    private final ProductMapper productMapper;

    public void saveProduct(CatalogEntity catalogEntity, Product product) {
        Optional<ProductEntity> productEntityOptional = productRepository.findByCatalogIdAndSku(catalogEntity.getId(), product.getSku());
        productEntityOptional.ifPresentOrElse(productEntity -> {
            productUpdater.updateProductEntity(productEntity,product);
            productRepository.save(productEntity);
        }, () -> {
            ProductEntity productEntity = productMapper.toProductEntity(product);
            productEntity.setCatalog(catalogEntity);
            productRepository.save(productEntity);
        });
    }

    public List<Product> getProductsForCatalog(UUID id) {
        return productRepository.findAllByCatalogId(id).stream()
            .map(productMapper::toProduct)
            .toList();
    }

    public void deleteAllProductsFor(UUID catalogEntityId, Set<String> productSkus) {
        productRepository.deleteByCatalogIdAndSkuIsIn(catalogEntityId, productSkus);
    }
}
