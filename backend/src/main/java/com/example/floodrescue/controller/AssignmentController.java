package com.example.floodrescue.controller;

import com.example.floodrescue.dto.ApiResponse;
import com.example.floodrescue.dto.AssignmentCreateRequest;
import com.example.floodrescue.model.Assignment;
import com.example.floodrescue.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COORDINATOR','ADMIN')")
    public ApiResponse<Assignment> create(@Valid @RequestBody AssignmentCreateRequest request) {
        return ApiResponse.success(assignmentService.create(request), "Assign team thành công");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COORDINATOR','MANAGER','ADMIN')")
    public ApiResponse<List<Assignment>> getAll() {
        return ApiResponse.success(assignmentService.getAll(), "Lấy danh sách assignment thành công");
    }

    @PatchMapping("/{id}/revoke")
    @PreAuthorize("hasAnyRole('COORDINATOR','ADMIN')")
    public ApiResponse<Assignment> revokeTask(@PathVariable Long id) {
        return ApiResponse.success(assignmentService.revokeTask(id), "Đã thu hồi lệnh và giải phóng lực lượng");
    }
}
