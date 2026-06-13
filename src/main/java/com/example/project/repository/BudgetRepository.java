
package com.example.project.repository;

import com.example.project.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget,Long>{
    List<Budget> findAllByUserId(Long id);
    Optional<Budget> findByBudgetDate(LocalDateTime budgetDate);
    Optional<Budget> findByBudgetDateBetween(LocalDateTime from, LocalDateTime to);
    Optional<Budget> findByCategoryName(String categoryName);
    Optional<Budget> findByUserIdAndCategoryName(Long userId, String name);
    Optional<Budget> findByUserIdAndCategoryId(Long userId, Long categoryId);
}
