package com.example.project.repository;

import com.example.project.model.Category;
import com.example.project.model.Transaction;
import com.example.project.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface
TransactionRepository extends JpaSpecificationRepository<Transaction, Long> {
    Optional<Transaction> findByAmountBetween(Double from, Double to);
    List<Transaction> findByType(String type);
    List<Transaction> findByUserIdAndCategoryName(Long id, String name);
    List<Transaction> findAllByUserId(Long id);
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.user.id = :userId")
    Double sumAmountByTypeAndUser( TransactionType type,Long userId);

}
