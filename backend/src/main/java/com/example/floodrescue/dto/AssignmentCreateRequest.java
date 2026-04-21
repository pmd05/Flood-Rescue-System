package com.example.floodrescue.dto;

import jakarta.validation.constraints.NotNull;

public class AssignmentCreateRequest {
    @NotNull
    private Long requestId;

    @NotNull
    private Long teamId;

    @NotNull
    private Long vehicleId;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
}
