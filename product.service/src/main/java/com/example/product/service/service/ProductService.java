package com.example.product.service.service;

import com.example.product.service.dto.ProductRequest;
import com.example.product.service.dto.ProductResponse;
import com.example.product.service.model.Product;
import com.example.product.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRespository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder().
                name(productRequest.name())
                .description((productRequest.description()))
                .skuCode(productRequest.skuCode())
                .price(productRequest.price())
                .build();
        productRespository.save(product);
        log.info("Product Created Successfully");
        return  new ProductResponse(product.getId(),product.getName(),product.getDescription(),product.getSkuCode(),product.getPrice());
    }

    public List<ProductResponse> getAllProducts() {

        return productRespository.findAll().stream().map(product ->
                new ProductResponse(product.getId(),product.getName(),product.getDescription(),product.getSkuCode(),product.getPrice())).toList();
    }

    public void deleteAllProducts(){

        productRespository.deleteAll();
    }
}
