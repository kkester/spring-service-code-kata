package io.pivotal.service.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findAllByCatalogId(UUID catalogId);
    Optional<ProductEntity> findByCatalogIdAndSku(UUID catalogId, String sku);
    void deleteAllByCatalogId(UUID catalogEntityId);
    void deleteAllByCatalogIdAndSkuIsNotIn(UUID catalogId, List<String> sku);
}
