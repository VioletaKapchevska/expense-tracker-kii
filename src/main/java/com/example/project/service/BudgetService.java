package com.example.project.service;

import com.example.project.model.Budget;
import com.example.project.model.BudgetPeriod;
import com.example.project.model.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BudgetService {
    Budget save(Long userId, Double amount, LocalDateTime dateSaved, String budgetPeriod, Category category);
    Budget update(Long budgetId, Double amount, LocalDateTime dateUpdated); // type can be expense or income, update accordingly
    List<Budget> listByUser(Long userId);

    public Map<Long, Double> getAllocatedAmountsByUser(Long userId);
}
