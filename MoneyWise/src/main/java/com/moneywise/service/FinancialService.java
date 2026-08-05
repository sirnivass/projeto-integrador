package com.moneywise.service;

import java.math.BigDecimal;
import java.util.List;

import com.moneywise.model.Transaction;
import com.moneywise.model.TransactionType;

public class FinancialService {

    public BigDecimal calculateTotalByType(List<Transaction> transactions, TransactionType type) {
        BigDecimal total = BigDecimal.ZERO;
        for (Transaction transaction : transactions){
            if (transaction.getType() == type)
                total = total.add(transaction.getAmount());
        }
        return total;
    }
    public BigDecimal calculateBalance(List<Transaction> transactions) {
        // Ficou grande né? Tentei resumir e deixar o arquivo enxuto.
        return calculateTotalByType(transactions, TransactionType.INCOME).subtract(calculateTotalByType(transactions, TransactionType.EXPENSE));
    }

}
