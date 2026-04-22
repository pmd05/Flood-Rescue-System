package com.example.floodrescue.repository;

import com.example.floodrescue.model.ReliefItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReliefItemRepository extends JpaRepository<ReliefItem, Long> {
}
