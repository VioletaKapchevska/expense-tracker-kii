package com.example.project.service.impl;

import com.example.project.model.Budget;
import com.example.project.model.Transaction;
import com.example.project.model.TransactionType;
import com.example.project.model.User;
import com.example.project.repository.BudgetRepository;
import com.example.project.repository.TransactionRepository;
import com.example.project.service.ChartsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChartsServiceImpl implements ChartsService {

    private final TransactionRepository transactionRepo;


    private final BudgetRepository budgetRepo;

    public ChartsServiceImpl(TransactionRepository transactionRepo, BudgetRepository budgetRepo) {
        this.transactionRepo = transactionRepo;
        this.budgetRepo = budgetRepo;
    }
    @Override
    public List<Object[]> getTransactionsLast5Days(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate fiveDaysAgo = today.minusDays(4); // includes today + 4 before

        List<Transaction> transactions = transactionRepo.findAllByUserId(userId)
                .stream()
                .filter(tx -> !tx.getDatum().isBefore(fiveDaysAgo)) // tx.getDate() is assumed to be LocalDate
                .toList();

        Map<LocalDate, Long> dateCountMap = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getDatum, Collectors.counting()));

        List<Object[]> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Ensure all 5 days are represented even if there are 0 transactions
        for (int i = 0; i < 5; i++) {
            LocalDate date = fiveDaysAgo.plusDays(i);
            long count = dateCountMap.getOrDefault(date, 0L);
            result.add(new Object[]{date.format(formatter), count});
        }

        return result;
    }
    @Override
    public List<Object[]> getTop3ExpenseCategories(Long userId) {
        return transactionRepo.findAllByUserId(userId).stream()
                .filter(tx -> tx.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(Transaction::getCategoryname,
                        Collectors.summingDouble(Transaction::getAmount)))
                .entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // sort descending
                .limit(3)
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .toList();
    }

    public List<Object[]> getTotalIncomeVsExpenses(Long userId) {
        Double totalIncome = transactionRepo.sumAmountByTypeAndUser(TransactionType.INCOME, userId);
        Double totalExpenses = transactionRepo.sumAmountByTypeAndUser(TransactionType.EXPENSE, userId);

        List<Object[]> chartData = new ArrayList<>();
        chartData.add(new Object[]{"Income", totalIncome != null ? totalIncome : 0});
        chartData.add(new Object[]{"Expense", totalExpenses != null ? totalExpenses : 0});
        return chartData;
    }
    public List<Object[]>  getCategoryExpenses(Long userid) {
        List<Transaction> allExpenses = transactionRepo.findAllByUserId(userid)
                .stream().filter(tx -> tx.getType() == TransactionType.EXPENSE)
                .toList();

        Map<String, Double> map = new HashMap<>();
        for (Transaction tx : allExpenses) {
            map.put(tx.getCategory().getName(), map.getOrDefault(tx.getCategory().getName(), 0.0) + tx.getAmount());
        }

        List<Object[]> chartData = new ArrayList<>();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            chartData.add(new Object[]{entry.getKey(), entry.getValue()});
        }

        return chartData;
    }

    public List<Budget> getAllBudgets(Long id)
    {
        return budgetRepo.findAllByUserId(id);
    }
    public Map<String, Double> getSpentAmounts(Long id)
    {

        List<Transaction> allExpenses = transactionRepo.findAllByUserId(id).stream()
                .filter(t -> "expense".equalsIgnoreCase(t.getType().toString()))
                .collect(Collectors.toList());

        return allExpenses.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategoryname,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }



}
