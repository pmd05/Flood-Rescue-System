package com.example.floodrescue.controller;

import com.example.floodrescue.dto.ApiResponse;
import com.example.floodrescue.repository.RescueTeamRepository;
import com.example.floodrescue.repository.VehicleRepository;
import com.example.floodrescue.service.AssignmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/lookups")
@PreAuthorize("hasAnyRole('COORDINATOR','MANAGER','ADMIN')")
public class LookupController {
    private final RescueTeamRepository rescueTeamRepository;
    private final VehicleRepository vehicleRepository;
    private final AssignmentService assignmentService;

    public LookupController(RescueTeamRepository rescueTeamRepository,
                            VehicleRepository vehicleRepository,
                            AssignmentService assignmentService) {
        this.rescueTeamRepository = rescueTeamRepository;
        this.vehicleRepository = vehicleRepository;
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> getLookups() {
        return ApiResponse.success(Map.of(
                "teams", assignmentService.getAvailableTeams(),
                "allTeams", rescueTeamRepository.findAll(),
                "vehicles", vehicleRepository.findAll()
        ), "Lấy lookup data thành công");
    }
}
