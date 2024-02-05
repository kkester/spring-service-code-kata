package io.pivotal.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductUpdater productUpdater;
    private final ProductMapper productMapper;

}
