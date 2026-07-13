package com.inventory.manager.controller;

import com.inventory.manager.model.Order;
import com.inventory.manager.model.OrderHistory;
import com.inventory.manager.model.LaborHistory;
import com.inventory.manager.model.Item;
import com.inventory.manager.model.Employee;
import com.inventory.manager.repository.OrderRepository;
import com.inventory.manager.repository.ItemRepository;
import com.inventory.manager.repository.EmployeeRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final EmployeeRepository employeeRepository;

    //Injecting BOTH repositories so we can save orders AND update inventory stock
    public OrderController(OrderRepository orderRepository, ItemRepository itemRepository, EmployeeRepository employeeRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/checkout")
    @Transactional
    public Order checkout(@RequestBody Order newOrder) {

        // 1. Process Physical Items (if any exist in the payload)
        if (newOrder.getOrderHistory() != null) {
            for (OrderHistory lineItem : newOrder.getOrderHistory()) {
                // Link the child back to the parent receipt
                lineItem.setOrder(newOrder);

                // Fetch the Master Item from the DB
                Item masterItem = itemRepository.findById(lineItem.getItem().getId()).orElseThrow();
                lineItem.setItem(masterItem);

                // Prevent negative inventory stock
                if (masterItem.getQuantity() < lineItem.getQuantity()){
                    throw new RuntimeException("Not enough stock for item: " + masterItem.getName());
                }

                // Subtract the quantity bought
                masterItem.setQuantity(masterItem.getQuantity() - lineItem.getQuantity());
                itemRepository.save(masterItem);
            }
        }

        // 2. Process Labor/Services
        if (newOrder.getLaborHistory() != null) {
            for (LaborHistory laborItem : newOrder.getLaborHistory()) {
                // This is the crucial line: Link the timesheet to the receipt!
                laborItem.setOrder(newOrder);

                // Fetch the fully loaded mechanic from the DB using the ID provided in Postman
                Employee mechanic = employeeRepository.findById(laborItem.getEmployee().getId())
                        .orElseThrow(() -> new RuntimeException("Mechanic not found!"));

                // Attach the verified mechanic to the timesheet
                laborItem.setEmployee(mechanic);
            }
        }

        // 3. Save the Order (Cascade will now successfully save history AND laborHistory without crashing!)
        return orderRepository.save(newOrder);
    }
}