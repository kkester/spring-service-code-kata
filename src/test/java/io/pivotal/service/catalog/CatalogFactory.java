package io.pivotal.service.catalog;

public class CatalogFactory {
    private CatalogFactory() {
    }

    public static CatalogEntity createCatalogEntity() {
        return CatalogEntity.builder()
            .code("xyz")
            .displayName("Sports")
            .build();
    }

    public static Catalog createCatalog() {
        return Catalog.builder()
            .code("xyz")
            .displayName("Sports")
            .build();
    }
}
