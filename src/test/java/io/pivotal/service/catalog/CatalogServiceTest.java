package io.pivotal.service.catalog;

import io.pivotal.service.product.Product;
import io.pivotal.service.product.ProductEntity;
import io.pivotal.service.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.pivotal.service.catalog.CatalogFactory.createCatalogEntity;
import static io.pivotal.service.product.ProductFactory.createProduct;
import static io.pivotal.service.product.ProductFactory.createProductEntity;
import static org.assertj.core.api.Assertions.assertThat;

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
    void getAll() {
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);

        List<Catalog> catalogs = catalogService.getAll();

        Catalog expectedCatalog = Catalog.builder()
            .code("xyz")
            .displayName("Sports")
            .build();
        assertThat(catalogs).containsExactly(expectedCatalog);
    }

    @Test
    void addProduct_successfullyAddsProductToCatalog() {
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);
        Product product = createProduct();

        Catalog catalog = catalogService.addProduct(product);

        Catalog expectedCatalog = Catalog.builder()
            .code("xyz")
            .displayName("Sports")
            .products(List.of(product))
            .build();
        assertThat(catalog).isEqualTo(expectedCatalog);
    }

    @Test
    void replaceProducts() {
        CatalogEntity catalogEntity = createCatalogEntity();
        catalogRepository.save(catalogEntity);
        ProductEntity productEntity = createProductEntity();
        productEntity.setCatalog(catalogEntity);
        productRepository.save(productEntity);

        Product existingProduct = createProduct();
        Product newProduct = createProduct().toBuilder()
            .sku("bcd")
            .build();

        Catalog catalog = catalogService.replaceProducts(List.of(newProduct, existingProduct));

        Catalog expectedCatalog = Catalog.builder()
            .code("xyz")
            .displayName("Sports")
            .build();
        assertThat(catalog).isEqualTo(expectedCatalog);
    }
}