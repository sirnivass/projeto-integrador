package com.moneywise.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;

import com.moneywise.model.Budget;
import com.moneywise.model.Transaction;
import com.moneywise.model.TransactionType;

public class BudgetService {
    
    public BigDecimal calculateCommitmentPercentage(List <Transaction> transactions, Budget budget) {
        BigDecimal spent = BigDecimal.ZERO;

        if (budget.getLimit().signum() <= 0)
                throw new IllegalArgumentException("Limite do orçamento deve ser um número positivo maior que zero");
        
        for (Transaction transaction : transactions) {
            boolean isExpense = (transaction.getType() == TransactionType.EXPENSE);
            boolean sameCategory = transaction.getCategory().equals(budget.getCategory());
            boolean sameMonth = YearMonth.from(transaction.getDate()).equals(budget.getMonth());

            if (isExpense && sameCategory && sameMonth)
                spent = spent.add(transaction.getAmount());
        }
        return spent.divide(budget.getLimit(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)); //Tive problemas com dízimas, por isso esse arredondamento.
    }
}
