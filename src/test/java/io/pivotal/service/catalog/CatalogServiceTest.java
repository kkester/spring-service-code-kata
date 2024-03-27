package io.pivotal.service.catalog;

import io.pivotal.service.errors.ResourceConflictException;
import io.pivotal.service.errors.ResourceNotFoundException;
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
        // arrange
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);

        // act
        List<Catalog> catalogs = catalogService.getAll();

        // assert
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
        // arrange
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);
        Product product = createProduct();

        // act
        Catalog catalog = catalogService.addProduct(catalogEntity.getCode(), product);

        // assert
        Catalog expectedCatalog = createCatalog().toBuilder()
            .products(List.of(product))
            .build();
        assertThat(catalog).isEqualTo(expectedCatalog);

        List<ProductEntity> productEntities = productRepository.findAllByCatalogId(catalogEntity.getId());
        assertThat(productEntities).hasSize(1);
        ProductEntity actualProductEntity = productEntities.get(0);
        assertThat(actualProductEntity.getSku()).isEqualTo(product.getSku());
        assertThat(actualProductEntity.getName()).isEqualTo(product.getName());
        assertThat(actualProductEntity.getDescription()).isEqualTo(product.getDescription());
        assertThat(actualProductEntity.getPrice()).isEqualTo(product.getPrice());
        assertThat(actualProductEntity.getQuantity()).isEqualTo(product.getQuantity());
    }

    @Test
    void addProduct_failsWhenCatalogAlreadyContainsProduct() {
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);
        ProductEntity productEntityToUpdate = createProductEntity();
        productEntityToUpdate.setCatalog(catalogEntity);
        productRepository.save(productEntityToUpdate);
        Product product = createProduct();

        Exception exception = assertThrows(ResourceConflictException.class, () ->
            catalogService.addProduct(catalogEntity.getCode(), product));

        assertThat(exception.getMessage()).isEqualToIgnoringCase("Product already exists");
    }

    @Test
    void replaceProducts_failsWhenCatalogIdIsInvalid() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
            catalogService.replaceProducts("invalidCode", null));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("Catalog not found");
    }

    @Test
    void replaceProducts_successfullyWhenProductDoesNotExist() {
        // arrange
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);

        Product newProduct = createProduct();
        List<Product> products = List.of(newProduct);

        // act
        Catalog catalog = catalogService.replaceProducts(catalogEntity.getCode(), products);

        // assert
        Catalog expectedCatalog = createCatalog().toBuilder()
            .products(products)
            .build();
        assertThat(catalog).isEqualTo(expectedCatalog);

        List<ProductEntity> productEntities = productRepository.findAllByCatalogId(catalogEntity.getId());
        assertThat(productEntities).hasSize(1);
        ProductEntity actualProductEntity = productEntities.get(0);
        assertThat(actualProductEntity.getSku()).isEqualTo(newProduct.getSku());
        assertThat(actualProductEntity.getName()).isEqualTo(newProduct.getName());
        assertThat(actualProductEntity.getDescription()).isEqualTo(newProduct.getDescription());
        assertThat(actualProductEntity.getPrice()).isEqualTo(newProduct.getPrice());
        assertThat(actualProductEntity.getQuantity()).isEqualTo(newProduct.getQuantity());
    }

    @Test
    void replaceProducts_successfullyWhenProductExists() {
        // arrange
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);
        ProductEntity productEntityToUpdate = createProductEntity();
        productEntityToUpdate.setCatalog(catalogEntity);
        productRepository.save(productEntityToUpdate);

        Product existingProduct = Product.builder()
            .sku(productEntityToUpdate.getSku())
            .name("Deformed Ball")
            .description("Roundish Ball")
            .price("$1.99")
            .quantity(11)
            .build();
        List<Product> products = List.of(existingProduct);

        // act
        catalogService.replaceProducts(catalogEntity.getCode(), products);

        // assert
        List<ProductEntity> productEntities = productRepository.findAllByCatalogId(catalogEntity.getId());
        assertThat(productEntities).hasSize(1);
        ProductEntity actualProductEntity = productEntities.get(0);
        assertThat(actualProductEntity.getId()).isEqualTo(productEntityToUpdate.getId());
        assertThat(actualProductEntity.getSku()).isEqualTo(existingProduct.getSku());
        assertThat(actualProductEntity.getName()).isEqualTo(existingProduct.getName());
        assertThat(actualProductEntity.getDescription()).isEqualTo(existingProduct.getDescription());
        assertThat(actualProductEntity.getPrice()).isEqualTo(existingProduct.getPrice());
        assertThat(actualProductEntity.getQuantity()).isEqualTo(existingProduct.getQuantity());
    }

    @Test
    void replaceProducts_successfullyWhenProductHasBeenRemoved() {
        // arrange
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);
        Product product = createProduct();
        ProductEntity productEntityToDelete = createProductEntity();
        productEntityToDelete.setSku("123");
        productEntityToDelete.setCatalog(catalogEntity);
        productRepository.save(productEntityToDelete);

        // act
        catalogService.replaceProducts(catalogEntity.getCode(), List.of(product));

        // assert
        List<ProductEntity> productEntities = productRepository.findAllByCatalogId(catalogEntity.getId());
        assertThat(productEntities).hasSize(1);
        assertThat(productEntities.get(0).getSku()).isEqualTo(product.getSku());
    }

    @Test
    void replaceProducts_successfullyWhenAllProductsHaveBeenRemoved() {
        // arrange
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);
        ProductEntity productEntityToDelete = createProductEntity();
        productEntityToDelete.setCatalog(catalogEntity);
        productRepository.save(productEntityToDelete);

        CatalogEntity anotherCatalogEntity = createCatalogEntity();
        anotherCatalogEntity.setCode("x1z");
        catalogRepository.save(anotherCatalogEntity);
        ProductEntity productEntity = createProductEntity();
        productEntity.setCatalog(anotherCatalogEntity);
        productRepository.save(productEntity);

        // act
        catalogService.replaceProducts(catalogEntity.getCode(), emptyList());

        // assert
        List<ProductEntity> productEntities = productRepository.findAllByCatalogId(catalogEntity.getId());
        assertThat(productEntities).isEmpty();
        List<ProductEntity> otherProductEntities = productRepository.findAllByCatalogId(anotherCatalogEntity.getId());
        assertThat(otherProductEntities).hasSize(1);
    }
}