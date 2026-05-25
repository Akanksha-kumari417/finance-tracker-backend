package com.finance.tracker.dto;

import com.finance.tracker.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private String description;
    private BigDecimal amount;
    private String category;
    private TransactionType type;
    private LocalDate date;
    private LocalDateTime createdAt;
}
