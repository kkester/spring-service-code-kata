package io.pivotal.service.catalog;

import org.springframework.stereotype.Component;

@Component
public class CatalogMapper {
    public Catalog toCatalog(CatalogEntity catalogEntity) {
        return Catalog.builder()
            .code(catalogEntity.getCode())
            .displayName(catalogEntity.getDisplayName())
            .build();
    }
}
