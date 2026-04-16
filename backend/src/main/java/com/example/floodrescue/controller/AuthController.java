package com.example.floodrescue.controller;

import com.example.floodrescue.dto.ApiResponse;
import com.example.floodrescue.dto.LoginRequest;
import com.example.floodrescue.dto.LoginResponse;
import com.example.floodrescue.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request), "Login thành công");
    }
}
