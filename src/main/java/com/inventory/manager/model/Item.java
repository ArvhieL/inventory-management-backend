package com.inventory.manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import jakarta.persistence.Transient;

@Entity
@Table(name= "inventory_items")

public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank (message = "Item name cannot be empty")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be less than zero")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull(message = "Cost is required")
    @Min(value = 0, message = "Cost cannot be negative")
    @Column(nullable = false)
    private BigDecimal cost;

    @NotBlank (message = "Item category cannot be empty")
    @Column(nullable = false)
    private String category;


    // Empty Constructor Required By Spring Data JPA
    public Item() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Transient
    public BigDecimal getTotalPrice() { return price.multiply(BigDecimal.valueOf(quantity)); }

    @Transient
    public  BigDecimal getTotalCost() { return cost.multiply(BigDecimal.valueOf(quantity)); }
}
