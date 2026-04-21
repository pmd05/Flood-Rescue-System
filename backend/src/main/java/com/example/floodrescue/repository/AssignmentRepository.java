package com.example.floodrescue.repository;

import com.example.floodrescue.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findAllByOrderByAssignedAtDesc();
    List<Assignment> findByTeam_IdOrderByAssignedAtDesc(Long teamId);
    boolean existsByRequest_IdAndStatusIgnoreCase(Long requestId, String status);
    boolean existsByVehicle_IdAndStatusIgnoreCase(Long vehicleId, String status);
    boolean existsByTeam_IdAndStatusIgnoreCase(Long teamId, String status);
    boolean existsByTeam_IdAndStatusIn(Long teamId, List<String> statuses);
    boolean existsByVehicle_IdAndStatusIn(Long vehicleId, List<String> statuses);
    boolean existsByVehicle_Id(Long vehicleId);
    boolean existsByTeam_Id(Long teamId);
}
