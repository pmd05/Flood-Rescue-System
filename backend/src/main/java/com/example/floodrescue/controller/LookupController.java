package com.example.floodrescue.controller;

import com.example.floodrescue.dto.ApiResponse;
import com.example.floodrescue.repository.VehicleRepository;
import com.example.floodrescue.service.AssignmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/lookups")
@PreAuthorize("hasAnyRole('COORDINATOR','MANAGER','ADMIN')")
public class LookupController {

    private final AssignmentService assignmentService;
    private final VehicleRepository vehicleRepository;

    public LookupController(AssignmentService assignmentService,
                            VehicleRepository vehicleRepository) {
        this.assignmentService = assignmentService;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> getLookups() {
        return ApiResponse.success(
                Map.of(
                        "teams", assignmentService.getAllTeams(),          // tất cả team (UI)
                        "availableTeams", assignmentService.getAvailableTeams(), // team available (assign)
                        "vehicles", vehicleRepository.findAll()
                ),
                "Lấy lookup data thành công"
        );
    }
}