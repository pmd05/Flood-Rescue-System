package com.example.floodrescue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RequestCreateRequest {
    @NotBlank
    private String description;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;

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
