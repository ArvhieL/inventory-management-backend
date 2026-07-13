package com.inventory.manager.repository;

import com.inventory.manager.model.LaborHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LaborHistoryRepository extends JpaRepository<LaborHistory, Long> {
    List<LaborHistory> findByEmployeeId(Long employeeId);
}