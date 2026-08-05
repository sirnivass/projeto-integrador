package com.moneywise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.moneywise.model.Category;
import com.moneywise.model.Transaction;
import com.moneywise.model.TransactionType;

public class FinancialServiceTest {
    private final FinancialService service = new FinancialService();
    private final Category salario = new Category(1, "Salário");
    private final Category alimentacao = new Category(2, "Alimentação");

    private Transaction receita(BigDecimal valor) {
        return new Transaction(1, TransactionType.INCOME, "Receita",
                valor, LocalDate.now(), salario);
    }

    private Transaction despesa(BigDecimal valor) {
        return new Transaction(2, TransactionType.EXPENSE, "Despesa",
                valor, LocalDate.now(), alimentacao);
    }

    @Test
    void deveCalcularSaldoComReceitasEDespesas() {
        // ARRANGE
        List<Transaction> transacoes = List.of(
                receita(new BigDecimal("1000")),
                despesa(new BigDecimal("300")));

        // ACT
        BigDecimal saldo = service.calculateBalance(transacoes);

        // ASSERT
        assertEquals(new BigDecimal("700"), saldo);
    }

    @Test
    void deveRetornarZeroParaListaVazia() {
        BigDecimal saldo = service.calculateBalance(List.of());
        assertEquals(BigDecimal.ZERO, saldo);
    }

    @Test
    void deveCalcularTotalPorTipo() {
        List<Transaction> transacoes = List.of(
                receita(new BigDecimal("1000")),
                despesa(new BigDecimal("300")),
                despesa(new BigDecimal("200")));

        BigDecimal totalDespesas = service.calculateTotalByType(transacoes, TransactionType.EXPENSE);
        assertEquals(new BigDecimal("500"), totalDespesas);
    }
}