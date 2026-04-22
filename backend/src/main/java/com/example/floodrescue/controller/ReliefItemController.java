package com.example.floodrescue.controller;

import com.example.floodrescue.dto.ApiResponse;
import com.example.floodrescue.dto.ReliefItemRequest;
import com.example.floodrescue.model.ReliefItem;
import com.example.floodrescue.service.ReliefItemService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relief-items")
@PreAuthorize("hasAnyRole('MANAGER','ADMIN','RESCUE_TEAM')")
public class ReliefItemController {
    private final ReliefItemService reliefItemService;

    public ReliefItemController(ReliefItemService reliefItemService) {
        this.reliefItemService = reliefItemService;
    }

    @PostMapping
    public ApiResponse<ReliefItem> create(@Valid @RequestBody ReliefItemRequest request) {
        return ApiResponse.success(reliefItemService.create(request), "Tạo relief item thành công");
    }

    @GetMapping
    public ApiResponse<List<ReliefItem>> getAll() {
        return ApiResponse.success(reliefItemService.getAll(), "Lấy danh sách relief items thành công");
    }

    @PutMapping("/{id}")
    public ApiResponse<ReliefItem> update(@PathVariable Long id, @Valid @RequestBody ReliefItemRequest request) {
        return ApiResponse.success(reliefItemService.update(id, request), "Cập nhật relief item thành công");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(@PathVariable Long id) {
        reliefItemService.delete(id);
        return ApiResponse.success(null, "Xóa relief item thành công");
    }
}
