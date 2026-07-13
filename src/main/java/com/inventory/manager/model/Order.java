package com.inventory.manager.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    // cascade = CascadeType.ALL - if we save the Order, it saves the History too
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderHistory> orderHistory = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<LaborHistory> laborHistory = new ArrayList<>();

    // Empty Constructor Required By Spring Data JPA
    public Order() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDate()  { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public List<OrderHistory> getOrderHistory() { return orderHistory; }
    public void setOrderHistory(List<OrderHistory> orderHistory) { this.orderHistory = orderHistory; }

    public List<LaborHistory> getLaborHistory() { return laborHistory; }
    public void setLaborHistory(List<LaborHistory> laborHistory) { this.laborHistory = laborHistory; }
}
