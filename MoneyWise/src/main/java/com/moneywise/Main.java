package com.moneywise;

import com.moneywise.repository.BudgetRepository;
import com.moneywise.repository.TransactionRepository;
import com.moneywise.ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        // Composição das dependências
        TransactionRepository transactionRepository = new TransactionRepository();
        BudgetRepository budgetRepository = new BudgetRepository();

        ConsoleMenu menu = new ConsoleMenu(transactionRepository, budgetRepository);
        menu.start();
    }
}