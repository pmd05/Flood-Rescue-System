package com.example.floodrescue.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rescue_teams")
public class RescueTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    public RescueTeam() {}

    public RescueTeam(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
