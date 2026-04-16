package com.example.floodrescue.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private RescueRequest request;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private RescueTeam team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(length = 50)
    private String status;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    @Column(name = "exception_note", columnDefinition = "NVARCHAR(MAX)")
    private String exceptionNote;

    @Column(name = "exception_image", columnDefinition = "NVARCHAR(MAX)")
    private String exceptionImage;

    @PrePersist
    public void onCreate() {
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public RescueRequest getRequest() {
        return request;
    }

    public void setRequest(RescueRequest request) {
        this.request = request;
    }

    public RescueTeam getTeam() {
        return team;
    }

    public void setTeam(RescueTeam team) {
        this.team = team;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public String getExceptionNote() {
        return exceptionNote;
    }

    public void setExceptionNote(String exceptionNote) {
        this.exceptionNote = exceptionNote;
    }

    public String getExceptionImage() {
        return exceptionImage;
    }

    public void setExceptionImage(String exceptionImage) {
        this.exceptionImage = exceptionImage;
    }
}
