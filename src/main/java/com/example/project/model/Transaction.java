package com.example.project.model;

import com.example.project.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private LocalDate datum;
    private String description;
    @ManyToOne
    private Category category;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public Transaction(double amount, LocalDate datum, String description, Category category, User user, TransactionType type) {
        this.amount = amount;
        this.datum = datum;
        this.description = description;
        this.category = category;
        this.user = user;
        this.type = type;
    }
    public String getCategoryname()
    {
        return category.getName();
    }
}
