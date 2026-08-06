package com.moneywise.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private final long id;
    private final TransactionType type;
    private final String description;
    private final BigDecimal amount;
    private final LocalDate date;
    private final Category category;
    
    
    public Transaction(long id, TransactionType type, String description, BigDecimal amount, LocalDate date, Category category) {
        this.id = id;
        this.type = type;
        this.description = description;
        if (amount.signum() <=0) // preferi usar signum pois retorna -1 ou 1
            throw new IllegalArgumentException("amount deve ser positivo");
        this.amount = amount;
        this.date = date;
        this.category = category;
    }
    
    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }


    public String getDescription() {
        return description;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public LocalDate getDate() {
        return date;
    }


    public Category getCategory() {
        return category;
    }    
}
