package com.example.floodrescue.repository;

import com.example.floodrescue.model.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
    boolean existsByItem_Id(Long itemId);
}
