package com.inventory.manager.controller;

import com.inventory.manager.model.Item;
import com.inventory.manager.repository.ItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class ItemController {

    //The Controller needs the Repository to talk to the database
    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // 1. GET REQUEST: View all items on the shelves
    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // 2. POST REQUEST: Add a new Item to the shelves
    @PostMapping
    public Item addItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    // 3. DELETE REQUEST: Remove an Item by its ID
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);
    }
}
