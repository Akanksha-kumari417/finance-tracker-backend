package com.finance.tracker.service;

import com.finance.tracker.dto.SummaryResponse;
import com.finance.tracker.dto.TransactionRequest;
import com.finance.tracker.dto.TransactionResponse;
import com.finance.tracker.model.Transaction;
import com.finance.tracker.model.TransactionType;
import com.finance.tracker.model.User;
import com.finance.tracker.repository.TransactionRepository;
import com.finance.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // Create new transaction
    public TransactionResponse createTransaction(String userEmail, TransactionRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setCategory(request.getCategory());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());

        Transaction saved = transactionRepository.save(transaction);

        return mapToResponse(saved);
    }

    // Get all transactions for user
    public List<TransactionResponse> getAllTransactions(String userEmail) {
        List<Transaction> transactions = transactionRepository.findByUserEmailOrderByDateDesc(userEmail);
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get transaction by ID
    public TransactionResponse getTransactionById(String userEmail, Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Verify transaction belongs to user
        if (!transaction.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized access");
        }

        return mapToResponse(transaction);
    }

    // Update transaction
    public TransactionResponse updateTransaction(String userEmail, Long id, TransactionRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Verify transaction belongs to user
        if (!transaction.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized access");
        }

        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setCategory(request.getCategory());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());

        Transaction updated = transactionRepository.save(transaction);
        return mapToResponse(updated);
    }

    // Delete transaction
    public void deleteTransaction(String userEmail, Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Verify transaction belongs to user
        if (!transaction.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized access");
        }

        transactionRepository.delete(transaction);
    }

    // Get monthly summary
    public SummaryResponse getMonthlySummary(String userEmail, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<Transaction> transactions = transactionRepository
                .findByUserEmailAndDateBetween(userEmail, startDate, endDate);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpenses);

        // Group expenses by category
        Map<String, BigDecimal> expensesByCategory = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        return new SummaryResponse(
                totalIncome,
                totalExpenses,
                balance,
                expensesByCategory,
                transactions.size()
        );
    }

    // Get transactions by category
    public List<TransactionResponse> getTransactionsByCategory(String userEmail, String category) {
        List<Transaction> transactions = transactionRepository.findByUserEmailAndCategory(userEmail, category);
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Helper method to convert Transaction to TransactionResponse
    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getType(),
                transaction.getDate(),
                transaction.getCreatedAt()
        );
    }
}
