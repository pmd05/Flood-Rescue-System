package com.example.floodrescue.controller;

import com.example.floodrescue.dto.*;
import com.example.floodrescue.model.User;
import com.example.floodrescue.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<List<User>> getAll() {
        return ApiResponse.success(userService.getAll(), "Lấy danh sách user thành công");
    }

    @PostMapping
    public ApiResponse<User> create(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.success(userService.create(request), "Tạo user thành công");
    }

    @PutMapping("/{id}")
    public ApiResponse<User> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return ApiResponse.success(userService.update(id, request), "Cập nhật user thành công");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success(null, "Xóa user thành công");
    }
}
