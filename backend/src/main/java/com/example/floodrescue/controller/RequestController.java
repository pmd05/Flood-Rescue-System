package com.example.floodrescue.controller;

import com.example.floodrescue.dto.ApiResponse;
import com.example.floodrescue.dto.RequestCreateRequest;
import com.example.floodrescue.dto.StatusUpdateRequest;
import com.example.floodrescue.model.RescueRequest;
import com.example.floodrescue.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CITIZEN')")
    public ApiResponse<RescueRequest> create(@Valid @RequestBody RequestCreateRequest request,
                                             Authentication authentication) {
        return ApiResponse.success(requestService.create(request, authentication.getName()), "Tạo rescue request thành công");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CITIZEN','COORDINATOR','MANAGER','ADMIN','RESCUE_TEAM')")
    public ApiResponse<List<RescueRequest>> getAll(Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", "").toLowerCase())
                .orElse("");
        return ApiResponse.success(requestService.getAll(authentication.getName(), role), "Lấy danh sách request thành công");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CITIZEN','COORDINATOR','MANAGER','ADMIN','RESCUE_TEAM')")
    public ApiResponse<RescueRequest> getById(@PathVariable Long id, Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", "").toLowerCase())
                .orElse("");
        return ApiResponse.success(requestService.getByIdAccessible(id, authentication.getName(), role), "Lấy chi tiết request thành công");
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('COORDINATOR','ADMIN')")
    public ApiResponse<RescueRequest> updateStatus(@PathVariable Long id,
                                                   @Valid @RequestBody StatusUpdateRequest request) {
        return ApiResponse.success(requestService.updateStatus(id, request), "Cập nhật trạng thái thành công");
    }

    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('COORDINATOR','ADMIN')")
    public ApiResponse<RescueRequest> verify(@PathVariable Long id) {
        return ApiResponse.success(requestService.verify(id), "Verify request thành công");
    }
}
