package io.pivotal.service.catalog;

import io.pivotal.service.product.Product;
import io.pivotal.service.product.ProductEntity;
import io.pivotal.service.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.pivotal.service.catalog.CatalogFactory.createCatalog;
import static io.pivotal.service.catalog.CatalogFactory.createCatalogEntity;
import static io.pivotal.service.product.ProductFactory.createProduct;
import static io.pivotal.service.product.ProductFactory.createProductEntity;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CatalogServiceTest {

    @Autowired
    CatalogService catalogService;

    @Autowired
    CatalogRepository catalogRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    void getAll_successfullyReturnsAllCatalogs() {
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);

        List<Catalog> catalogs = catalogService.getAll();

        Catalog expectedCatalog = createCatalog();
        assertThat(catalogs).containsExactly(expectedCatalog);
    }

    @Test
    void addProduct_failsWhenCatalogIdIsInvalid() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
            catalogService.addProduct("invalidCode", null));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("Catalog not found");
    }

    @Test
    void addProduct_successfullyAddsProductToCatalog() {
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);
        Product product = createProduct();

        Catalog catalog = catalogService.addProduct(catalogEntity.getCode(), product);

        Catalog expectedCatalog = createCatalog().toBuilder()
            .products(List.of(product))
            .build();
        assertThat(catalog).isEqualTo(expectedCatalog);

        List<ProductEntity> productEntities = productRepository.findAllByCatalogId(catalogEntity.getId());
        assertThat(productEntities).hasSize(1);
        assertThat(productEntities.get(0).getSku()).isEqualTo(product.getSku());
    }

    @Test
    void replaceProducts_failsWhenCatalogIdIsInvalid() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
            catalogService.replaceProducts("invalidCode", emptyList()));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("Catalog not found");
    }

    @Test
    void replaceProducts_successfullyWhenProductDoesNotExist() {
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);

        Product newProduct = createProduct();
        List<Product> products = List.of(newProduct);

        Catalog catalog = catalogService.replaceProducts(catalogEntity.getCode(), products);

        Catalog expectedCatalog = createCatalog().toBuilder()
            .products(products)
            .build();
        assertThat(catalog).isEqualTo(expectedCatalog);

        List<ProductEntity> productEntities = productRepository.findAllByCatalogId(catalogEntity.getId());
        assertThat(productEntities).hasSize(1);
        assertThat(productEntities.get(0).getSku()).isEqualTo(newProduct.getSku());
    }

    @Test
    void replaceProducts_successfullyWhenProductExists() {
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);
        ProductEntity productEntityToUpdate = createProductEntity();
        productEntityToUpdate.setCatalog(catalogEntity);
        productRepository.save(productEntityToUpdate);

        Product existingProduct = createProduct().toBuilder()
            .price("$1.99")
            .build();
        List<Product> products = List.of(existingProduct);

        catalogService.replaceProducts(catalogEntity.getCode(), products);

        List<ProductEntity> productEntities = productRepository.findAllByCatalogId(catalogEntity.getId());
        assertThat(productEntities).hasSize(1);
        assertThat(productEntities.get(0).getId()).isEqualTo(productEntityToUpdate.getId());
        assertThat(productEntities.get(0).getPrice()).isEqualTo(existingProduct.getPrice());
    }

    @Test
    void replaceProducts_successfullyWhenProductHasBeenRemoved() {
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);
        Product product = createProduct();
        ProductEntity productEntityToDelete = createProductEntity();
        productEntityToDelete.setSku("123");
        productEntityToDelete.setCatalog(catalogEntity);
        productRepository.save(productEntityToDelete);

        catalogService.replaceProducts(catalogEntity.getCode(), List.of(product));

        List<ProductEntity> productEntities = productRepository.findAllByCatalogId(catalogEntity.getId());
        assertThat(productEntities).hasSize(1);
        assertThat(productEntities.get(0).getSku()).isEqualTo(product.getSku());
    }

    @Test
    void replaceProducts_successfullyWhenAllProductsHaveBeenRemoved() {
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);
        ProductEntity productEntityToDelete = createProductEntity();
        productEntityToDelete.setSku("123");
        productEntityToDelete.setCatalog(catalogEntity);
        productRepository.save(productEntityToDelete);

        catalogService.replaceProducts(catalogEntity.getCode(), emptyList());

        List<ProductEntity> productEntities = productRepository.findAllByCatalogId(catalogEntity.getId());
        assertThat(productEntities).isEmpty();
    }
}