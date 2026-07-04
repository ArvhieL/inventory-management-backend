package com.inventory.manager.repository;

import com.inventory.manager.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    // gives built-in methods like .save(), .findAll(), and .findById().
}