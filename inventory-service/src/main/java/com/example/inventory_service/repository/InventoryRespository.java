package com.example.inventory_service.repository;

import com.example.inventory_service.model.Inventory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRespository extends JpaRepository<Inventory, Long> {
     boolean existsByskuCodeAndQuantityIsGreaterThanEqual(String skuCode, Integer quantity);
}
