package com.example.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.YearMonth;

import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Data
@NoArgsConstructor
public class Budget{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    @ManyToOne
    private Category category;
    @ManyToOne
    private User user;
    private LocalDateTime budgetDate;
    private String description;
    @Enumerated(EnumType.STRING)
    private BudgetPeriod budgetPeriod;

    public Budget(User user,Double amount, LocalDateTime budgetDate, BudgetPeriod budgetPeriod, Category category)
    {
        this.amount = amount;
        this.user = user;
        this.budgetDate = budgetDate;
        this.budgetPeriod = budgetPeriod;
        this.category = category;

    }
}
