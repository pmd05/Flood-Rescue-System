package com.example.floodrescue.repository;

import com.example.floodrescue.model.RescueRequest;
import com.example.floodrescue.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RescueRequestRepository extends JpaRepository<RescueRequest, Long> {
    List<RescueRequest> findByUserOrderByCreatedAtDesc(User user);
    List<RescueRequest> findAllByOrderByCreatedAtDesc();
    boolean existsByUser_Id(Long userId);
}
