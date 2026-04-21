package com.example.floodrescue.model;

import jakarta.persistence.*;

@Entity
@Table(name = "relief_items")
public class ReliefItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private Integer quantity;

    @Column(name = "initial_quantity")
    private Integer initialQuantity;

    public ReliefItem() {}

    public ReliefItem(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
        this.initialQuantity = quantity;
    }

    @PrePersist
    public void onCreate() {
        if (this.initialQuantity == null) {
            this.initialQuantity = this.quantity;
        }
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

    public Integer getQuantity() {
        return quantity == null ? 0 : quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getInitialQuantity() {
        return initialQuantity == null ? (quantity == null ? 0 : quantity) : initialQuantity;
    }

    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }
}
