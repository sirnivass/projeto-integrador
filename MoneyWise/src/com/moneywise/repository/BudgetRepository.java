package com.moneywise.repository;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.moneywise.model.Budget;
import com.moneywise.model.Category;

public class BudgetRepository {
    private final List<Budget> budgets = new ArrayList<>();

    public void add(Budget budget) {
        budgets.add(budget);
    }

    public List<Budget> findAll() {
        return new ArrayList<>(budgets);
    }

    public Optional<Budget> findByCategoryAndMonth(Category category, YearMonth month) {
        return budgets.stream()
                .filter(b -> b.getCategory().equals(category)
                           && b.getMonth().equals(month))   // categoria E mês
                .findFirst();
    }
}