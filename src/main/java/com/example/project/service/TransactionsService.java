package com.example.project.service;

import com.example.project.model.Transaction;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionsService {
      Page<Transaction> findPage(Double from, Double to, String type, String categoryName, Integer pageNum, Integer pageSize);
      List<Transaction> listAllByUser(Long id);
      void save(Long userId, String category, String type, Double amount, String description, LocalDate date);
//    Optional<Transaction> findByAmountBetween(Double from, Double to);
//    List<Transaction> findByType(String type);
//    List<Transaction> findByUserIdAndCategoryName(Long id, String name);
}
