package com.example.floodrescue.dto;

import jakarta.validation.constraints.NotBlank;

public class VehicleRequest {
    @NotBlank
    private String name;
    private String status = "available";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status == null || status.isBlank() ? "available" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
