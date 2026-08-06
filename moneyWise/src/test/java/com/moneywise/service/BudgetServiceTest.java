package com.moneywise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.moneywise.model.Budget;
import com.moneywise.model.Category;
import com.moneywise.model.Transaction;
import com.moneywise.model.TransactionType;

public class BudgetServiceTest {
    
    private final BudgetService service = new BudgetService();
    private final Category alimentacao = new Category(1, "Alimentação");

    @Test
    void deveCalcularComprometimentoExato() {
        Budget orcamento = new Budget(1, alimentacao, YearMonth.now(), new BigDecimal("500"));
        Transaction gasto = new Transaction(1, TransactionType.EXPENSE, "Mercado",
                new BigDecimal("300"), LocalDate.now(), alimentacao);

        BigDecimal pct = service.calculateCommitmentPercentage(List.of(gasto), orcamento);

        assertEquals(new BigDecimal("60.0000"), pct);
    }

    @Test
    void deveIgnorarTransacoesDeOutraCategoria() {
        Category transporte = new Category(2, "Transporte");
        Budget orcamento = new Budget(1, alimentacao, YearMonth.now(), new BigDecimal("500"));
        Transaction gastoOutro = new Transaction(1, TransactionType.EXPENSE, "Ônibus",
                new BigDecimal("300"), LocalDate.now(), transporte);

        BigDecimal pct = service.calculateCommitmentPercentage(List.of(gastoOutro), orcamento);

        // compareTo compara VALOR, ignorando a escala (0 == 0.0000)
        assertEquals(0, pct.compareTo(BigDecimal.ZERO));
    }

    @Test
    void deveLancarExcecaoComLimiteZero() {
        Budget orcamentoZero = new Budget(1, alimentacao, YearMonth.now(), BigDecimal.ZERO);
        Transaction gasto = new Transaction(1, TransactionType.EXPENSE, "Mercado",
                new BigDecimal("300"), LocalDate.now(), alimentacao);

        assertThrows(IllegalArgumentException.class, () ->
                service.calculateCommitmentPercentage(List.of(gasto), orcamentoZero));
    }
}