package com.inventory.manager.controller;

import com.inventory.manager.model.Order;
import com.inventory.manager.model.OrderHistory;
import com.inventory.manager.model.Item;
import com.inventory.manager.repository.OrderRepository;
import com.inventory.manager.repository.ItemRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    //Injecting BOTH repositories so we can save orders AND update inventory stock
    public OrderController(OrderRepository orderRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    @PostMapping("/checkout")
    @Transactional //If any step fails, undo everything!
    public Order checkout(@RequestBody Order newOrder) {

        //Loop through all the physical items sold in this receipt
        for (OrderHistory lineItem : newOrder.getHistory()) {

            // 1. Link the child (History) back to the parent (Order)
            lineItem.setOrder(newOrder);

            // 2. Fetch the Master Item from the DB to check current stock
            Item masterItem = itemRepository.findById(lineItem.getItem().getId()).orElseThrow();
            lineItem.setItem(masterItem);

            // Prevent negative inventory stock
            if (masterItem.getQuantity() < lineItem.getQuantity()){
                throw new RuntimeException("Not enough stock for item: " + masterItem.getName());
            }

            // 3. Subtract the quantity bought from the master stock
            masterItem.setQuantity(masterItem.getQuantity() - lineItem.getQuantity());

            // 4. Save the updated Master Item back to the database
            itemRepository.save(masterItem);
        }
        // 5. Save the Order. Because we used CascadeType.ALL on the Order Blueprint,
        // this single line ALSO save everthing in the OrderHistory table automatically!
        return orderRepository.save(newOrder);
    }
}