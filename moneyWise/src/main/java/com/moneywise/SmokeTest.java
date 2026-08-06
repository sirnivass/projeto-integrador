package com.moneywise;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.moneywise.model.*;
import com.moneywise.service.BudgetService;
import com.moneywise.service.FinancialService;

public class SmokeTest {
        public static void main(String[] args) {
        // 1. Montar cenário
        Category alimentacao = new Category(1, "Alimentação");
        Category salario = new Category(2, "Salário");


        Transaction receita = new Transaction(1, TransactionType.INCOME, "Salário", new BigDecimal("1000"), LocalDate.now(), salario);
        Transaction despesa = new Transaction(2, TransactionType.EXPENSE, "Mercado", new BigDecimal("300"), LocalDate.now(), alimentacao);
        Transaction despesaCara = new Transaction(3, TransactionType.EXPENSE, "Aluguel", new BigDecimal("600"), LocalDate.now(), alimentacao);
        Budget orcamento = new Budget(1, alimentacao, YearMonth.now(), new BigDecimal("500"));

        List<Transaction> transactions = List.of(receita, despesa);

        // 2. Chamar os serviços
        FinancialService financialService = new FinancialService();
        BudgetService budgetService = new BudgetService();
        BigDecimal saldo = financialService.calculateBalance(transactions);
        BigDecimal soReceitas = financialService.calculateBalance(List.of(receita));
        BigDecimal vazio = financialService.calculateBalance(List.of());
        BigDecimal soDespesas = financialService.calculateBalance(List.of(despesa));
        BigDecimal pctAlta = budgetService.calculateCommitmentPercentage(List.of(despesaCara), orcamento);
        BigDecimal pctExato = budgetService.calculateCommitmentPercentage(transactions, orcamento);

        // 3. Verificar (assert)
        check("Saldo deve ser 700", saldo.compareTo(new BigDecimal("700")) == 0);
        check("Saldo apenas receitas deve ser 1000",soReceitas.compareTo(new BigDecimal("1000")) == 0);
        check("Saldo lista vazia deve ser 0",vazio.compareTo(BigDecimal.ZERO) == 0);
        check("Comprometimento exato deve ser 60%",pctExato.compareTo(new BigDecimal("60.0000")) == 0);
        check("Saldo apenas despesas deve ser -300",soDespesas.compareTo(new BigDecimal("-300")) == 0);
        check("Comprometimento acima do limite deve ser 120%",pctAlta.compareTo(new BigDecimal("120.0000")) == 0);

    }

    private static void check(String description, boolean condition) {
        if (condition) {
            System.out.println("✔ PASS - " + description);
        } else {
            System.out.println("✘ FAIL - " + description);
            System.exit(1);
        }
    }
}
