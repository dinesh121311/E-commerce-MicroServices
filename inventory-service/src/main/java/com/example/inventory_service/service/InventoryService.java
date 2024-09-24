package com.example.inventory_service.service;


import com.example.inventory_service.repository.InventoryRespository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private InventoryRespository inventoryRespository;


    public boolean isInStock(String skuCode, Integer quantity) {

        return inventoryRespository.existsByskuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }
}
