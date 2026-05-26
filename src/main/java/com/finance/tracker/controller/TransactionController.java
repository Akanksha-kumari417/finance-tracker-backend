package com.finance.tracker.controller;

import com.finance.tracker.dto.SummaryResponse;
import com.finance.tracker.dto.TransactionRequest;
import com.finance.tracker.dto.TransactionResponse;
import com.finance.tracker.security.JwtUtil;
import com.finance.tracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JwtUtil jwtUtil;

    // Extract user email from JWT token
    private String extractUserEmail(String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer "
        return jwtUtil.extractEmail(token);
    }

    // Create transaction
    @PostMapping
    public ResponseEntity<?> createTransaction(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody TransactionRequest request) {
        try {
            String userEmail = extractUserEmail(authHeader);
            TransactionResponse response = transactionService.createTransaction(userEmail, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get all transactions
    @GetMapping
    public ResponseEntity<?> getAllTransactions(@RequestHeader("Authorization") String authHeader) {
        try {
            String userEmail = extractUserEmail(authHeader);
            List<TransactionResponse> transactions = transactionService.getAllTransactions(userEmail);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // Get transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            String userEmail = extractUserEmail(authHeader);
            TransactionResponse transaction = transactionService.getTransactionById(userEmail, id);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Update transaction
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        try {
            String userEmail = extractUserEmail(authHeader);
            TransactionResponse response = transactionService.updateTransaction(userEmail, id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Delete transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            String userEmail = extractUserEmail(authHeader);
            transactionService.deleteTransaction(userEmail, id);
            return ResponseEntity.ok("Transaction deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get monthly summary
    @GetMapping("/summary")
    public ResponseEntity<?> getMonthlySummary(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        try {
            String userEmail = extractUserEmail(authHeader);

            // Default to current month if not provided
            LocalDate now = LocalDate.now();
            int targetYear = year != null ? year : now.getYear();
            int targetMonth = month != null ? month : now.getMonthValue();

            SummaryResponse summary = transactionService.getMonthlySummary(userEmail, targetYear, targetMonth);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get transactions by category
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getTransactionsByCategory(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String category) {
        try {
            String userEmail = extractUserEmail(authHeader);
            List<TransactionResponse> transactions = transactionService.getTransactionsByCategory(userEmail, category);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
