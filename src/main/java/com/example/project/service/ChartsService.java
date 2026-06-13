package com.example.project.service;

import com.example.project.model.Budget;
import com.example.project.model.User;

import java.util.List;
import java.util.Map;

public interface ChartsService {
    public List<Object[]>getTotalIncomeVsExpenses(Long id);
    public List<Object[]>  getCategoryExpenses(Long id);
   // public List<Map<String, Object>> getBudgetProgress();
   public List<Budget> getAllBudgets(Long id);
    public Map<String, Double> getSpentAmounts(Long id);
    public List<Object[]> getTop3ExpenseCategories(Long userId);
    public List<Object[]> getTransactionsLast5Days(Long userId) ;

}
