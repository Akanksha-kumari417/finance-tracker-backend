package com.finance.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
public class SummaryResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal balance;
    private Map<String, BigDecimal> expensesByCategory;
    private int transactionCount;
}
