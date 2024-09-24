package com.example.product.service.dto;

public record ProductResponse(Long id, String name, String description, String skuCode, double price) {
}
