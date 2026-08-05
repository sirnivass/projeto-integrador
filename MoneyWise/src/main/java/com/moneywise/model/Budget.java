package com.moneywise.model;

import java.math.BigDecimal;
import java.time.YearMonth;

public class Budget {
    private final long id;
    private final Category category;
    private final YearMonth month;     // ex: 2026-07
    private final BigDecimal limit;    // limite de gastos do mês
    
    public Budget(long id, Category category, YearMonth month, BigDecimal limit) {
        this.id = id;
        this.category = category;
        this.month = month;
        this.limit = limit;
    }
    
    public long getId() {
        return id;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public YearMonth getMonth() {
        return month;
    }
    
    public BigDecimal getLimit() {
        return limit;
    }

}
