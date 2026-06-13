package com.example.project.service.impl;

import com.example.project.exceptions.BudgetNotFoundException;
import com.example.project.exceptions.UserNotFoundException;
import com.example.project.model.Budget;
import com.example.project.model.BudgetPeriod;
import com.example.project.model.Category;
import com.example.project.model.User;
import com.example.project.repository.BudgetRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.BudgetService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Budget save(Long userId, Double amount, LocalDateTime dateSaved, String budgetPeriod, Category category) {
        Optional<Budget> existing = budgetRepository
                .findByUserIdAndCategoryId(userId, category.getId());
        User u = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Budget budget = existing.orElse(new Budget());
        budget.setUser(u);
        budget.setCategory(category);
        budget.setBudgetPeriod(BudgetPeriod.valueOf(budgetPeriod));
        budget.setAmount(amount);
        budget.setBudgetDate(dateSaved);

        return budgetRepository.save(budget);

//        User u = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
//        Budget b = new Budget(u, amount, dateSaved, budgetPeriod, category);
//        return budgetRepository.save(b);
    }

    @Override
    public Budget update(Long budgetId, Double amount, LocalDateTime dateUpdated) {
        Budget b = budgetRepository.findById(budgetId).orElseThrow(BudgetNotFoundException::new);
        b.setAmount(amount);
        b.setBudgetDate(dateUpdated);
        return budgetRepository.save(b);
    }

    @Override
    public List<Budget> listByUser(Long userId) {
        return budgetRepository.findAllByUserId(userId);
    }



    @Override
    public Map<Long, Double> getAllocatedAmountsByUser(Long userId) {
        List<Budget> budgets = budgetRepository.findAllByUserId(userId);
        return budgets.stream()
                .collect(Collectors.toMap(
                        b -> b.getCategory().getId(),
                        Budget::getAmount
                ));
    }
}
