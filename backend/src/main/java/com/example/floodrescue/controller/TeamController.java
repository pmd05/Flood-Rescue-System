package com.example.floodrescue.controller;

import com.example.floodrescue.dto.ApiResponse;
import com.example.floodrescue.model.Assignment;
import com.example.floodrescue.service.AssignmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team/tasks")
@PreAuthorize("hasRole('RESCUE_TEAM')")
public class TeamController {
    private final AssignmentService assignmentService;

    public TeamController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ApiResponse<List<Assignment>> getTasks(Authentication authentication) {
        return ApiResponse.success(assignmentService.getTeamTasks(authentication.getName()), "Lấy danh sách nhiệm vụ thành công");
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Assignment> completeTask(@PathVariable Long id, Authentication authentication) {
        return ApiResponse.success(assignmentService.completeTask(id, authentication.getName()), "Hoàn thành nhiệm vụ thành công");
    }

    @PatchMapping("/{id}/exception")
    public ApiResponse<Assignment> reportException(@PathVariable Long id, 
                                                   @jakarta.validation.Valid @RequestBody com.example.floodrescue.dto.AssignmentExceptionRequest request, 
                                                   Authentication authentication) {
        return ApiResponse.success(assignmentService.reportException(id, authentication.getName(), request), "Đã ghi nhận báo cáo sự cố");
    }
}
