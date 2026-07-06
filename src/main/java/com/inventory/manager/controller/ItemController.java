package com.inventory.manager.controller;

import com.inventory.manager.model.Item;
import com.inventory.manager.repository.ItemRepository;
import jakarta.validation.Valid;
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
    public Item addItem(@Valid @RequestBody Item item) {
        return itemRepository.save(item);
    }

    // 3. DELETE REQUEST: Remove an Item by its ID
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);
    }

    // 4. PUT REQUEST: Update an existing item
    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @Valid @RequestBody Item updatedDetails){

        // Find the existing item by its ID. (Throws an error if it doesn't exist).
        Item existingItem = itemRepository.findById(id).orElseThrow();

        // Overwrite the old data with the new data from the user
        existingItem.setName(updatedDetails.getName());
        existingItem.setQuantity(updatedDetails.getQuantity());
        existingItem.setPrice(updatedDetails.getPrice());

        // Save the updated item back to the database
        return itemRepository.save(existingItem);
    }

}
