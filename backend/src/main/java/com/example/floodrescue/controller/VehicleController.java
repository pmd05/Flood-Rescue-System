package com.example.floodrescue.controller;

import com.example.floodrescue.dto.ApiResponse;
import com.example.floodrescue.dto.VehicleRequest;
import com.example.floodrescue.model.Vehicle;
import com.example.floodrescue.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ApiResponse<Vehicle> create(@Valid @RequestBody VehicleRequest request) {
        return ApiResponse.success(vehicleService.create(request), "Tạo vehicle thành công");
    }

    @GetMapping
    public ApiResponse<List<Vehicle>> getAll() {
        return ApiResponse.success(vehicleService.getAll(), "Lấy danh sách vehicle thành công");
    }

    @PutMapping("/{id}")
    public ApiResponse<Vehicle> update(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        return ApiResponse.success(vehicleService.update(id, request), "Cập nhật vehicle thành công");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ApiResponse.success(null, "Xóa vehicle thành công");
    }
}
