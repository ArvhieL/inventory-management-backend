package com.inventory.manager.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "labor_history")
public class LaborHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private BigDecimal laborCharge;

    @Column
    private String description;

    // Empty Constructor Required By Spring Data JPA
    public LaborHistory() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public BigDecimal getLaborCharge() { return laborCharge; }
    public void setLaborCharge(BigDecimal laborCharge) { this.laborCharge = laborCharge; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

}
