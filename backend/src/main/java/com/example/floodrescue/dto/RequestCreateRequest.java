package com.example.floodrescue.dto;

import jakarta.validation.constraints.*;

public class RequestCreateRequest {

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Latitude không được null")
    @Min(value = -90, message = "Latitude không hợp lệ")
    @Max(value = 90, message = "Latitude không hợp lệ")
    private Double lat;

    @NotNull(message = "Longitude không được null")
    @Min(value = -180, message = "Longitude không hợp lệ")
    @Max(value = 180, message = "Longitude không hợp lệ")
    private Double lng;

    @Pattern(regexp = "low|medium|high", message = "Priority phải là low, medium hoặc high")
    private String priority = "medium";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getPriority() {
        return priority == null || priority.isBlank() ? "medium" : priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}