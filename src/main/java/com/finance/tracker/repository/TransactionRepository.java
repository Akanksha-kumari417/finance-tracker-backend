package com.finance.tracker.repository;

import com.finance.tracker.model.Transaction;
import com.finance.tracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find all transactions for a specific user
    List<Transaction> findByUserEmailOrderByDateDesc(String email);

    // Find transactions by user and type
    List<Transaction> findByUserEmailAndType(String email, TransactionType type);

    // Find transactions by user and date range
    List<Transaction> findByUserEmailAndDateBetween(String email, LocalDate startDate, LocalDate endDate);

    // Find transactions by user and category
    List<Transaction> findByUserEmailAndCategory(String email, String category);

    // Check if transaction belongs to user
    boolean existsByIdAndUserEmail(Long id, String email);
}
