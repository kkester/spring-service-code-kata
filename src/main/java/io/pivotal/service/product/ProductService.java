package io.pivotal.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductEntity> getProductsByCatalogId(UUID catalogId) {
        return productRepository.findAllByCatalogId(catalogId);
    }

    public void saveProductEntityWithProduct(ProductEntity productEntity, Product product) {
        productMapper.updateProductEntity(productEntity, product);
        productRepository.save(productEntity);
    }

    public void deleteAllProductEntities(Collection<ProductEntity> productEntities) {
        productRepository.deleteAll(productEntities);
    }

    public void deleteAllProductsByCatalogId(UUID catalogId) {
        productRepository.deleteAllByCatalogId(catalogId);
    }
}
