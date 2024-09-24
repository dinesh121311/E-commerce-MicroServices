package com.example.product.service.dto;

import java.math.BigDecimal;

public record ProductRequest(long id, String name, String description,String skuCode,double price) {
}
