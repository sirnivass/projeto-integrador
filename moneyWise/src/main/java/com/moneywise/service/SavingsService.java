package com.moneywise.service;

import java.math.BigDecimal;

public class SavingsService {
    public BigDecimal projectSavings(BigDecimal capital, BigDecimal monthlyRate, int months) {
        if (capital.signum() <= 0)
            throw new IllegalArgumentException("Capital deve ser positivo");
        if (monthlyRate.signum() <= 0) 
            throw new IllegalArgumentException("Taxa deve ser positiva");
        if (months < 0) {
            throw new IllegalArgumentException("Meses não pode ser negativo");
        }

        BigDecimal growthFactor = BigDecimal.ONE.add(monthlyRate);   // (1 + taxa)
        BigDecimal powered = growthFactor.pow(months);               // (1 + taxa)^meses
        return capital.multiply(powered);                            // capital × isso
    }
}
