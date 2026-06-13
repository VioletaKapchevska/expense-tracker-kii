package com.example.project.service.impl;

import com.example.project.exceptions.BudgetNotFoundException;
import com.example.project.exceptions.CategoryNotFoundException;
import com.example.project.exceptions.TransactionNotValidException;
import com.example.project.exceptions.UserNotFoundException;
import com.example.project.model.*;
import com.example.project.repository.BudgetRepository;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.TransactionRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.TransactionsService;
import jakarta.transaction.TransactionalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.project.service.specifications.FieldFilterSpecification.*;

@Service
public class TransactionsServiceImpl implements TransactionsService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    public TransactionsServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository,
                                   BudgetRepository budgetRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
    }

    // TODO: 10.6.2025 IMPLEMENT SEARCH BY CATEGORY AND DATE
    @Override
    public Page<Transaction> findPage(Double from, Double to, String type, String categoryName, Integer pageNum, Integer pageSize) {
        Specification<Transaction> specification = Specification.where(null);

        if (categoryName != null && !categoryName.isBlank()) {
            specification = specification.and(
                    filterContainsText(Transaction.class, "category.name", categoryName)
            );
        }

        if (from != null && to != null) {
            specification = specification.and(
                    filterBetween(Transaction.class, "amount", from, to)
            );
        }

        if (type != null && !type.isBlank()) {
            specification = specification.and(
                    filterEqualsV(Transaction.class, "type", type)
            );
        }
        return this.transactionRepository.findAll(
                specification,
                PageRequest.of(pageNum, pageSize) // vo kontrolerot -1
        );
    }

    @Override
    public List<Transaction> listAllByUser(Long id) {
        return transactionRepository.findAllByUserId(id);
    }

    @Override
    public void save(Long userId, String category, String type, Double amount, String description, LocalDate date) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Category c = categoryRepository.findByName(category).orElseThrow(CategoryNotFoundException::new);
        Budget userBudget = budgetRepository.findByUserIdAndCategoryName(userId, category).orElseThrow(BudgetNotFoundException::new);
        TransactionType transactionType = TransactionType.valueOf(type.toUpperCase());

        Double budgetAmount = userBudget.getAmount();

        if(type.equalsIgnoreCase("EXPENSE")){
            if((budgetAmount - amount) < 0 ) throw new TransactionNotValidException(String.format("Not enough budget amount for category %s", category));
            userBudget.setAmount(budgetAmount - amount);
        }else{
            userBudget.setAmount(budgetAmount + amount);
        }
        budgetRepository.save(userBudget);
        Transaction t = new Transaction(amount, date, description, c, user, transactionType);
        transactionRepository.save(t);
    }

}
