package com.moneywise.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.moneywise.model.Budget;
import com.moneywise.model.Category;
import com.moneywise.model.Transaction;
import com.moneywise.model.TransactionType;
import com.moneywise.repository.BudgetRepository;
import com.moneywise.repository.TransactionRepository;
import com.moneywise.service.BudgetService;
import com.moneywise.service.FinancialService;
import com.moneywise.service.SavingsService;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final FinancialService financialService = new FinancialService();
    private final BudgetService budgetService = new BudgetService();
    private final SavingsService savingsService = new SavingsService();

    // Categorias padrão do sistema (sem banco por enquanto)
    private static final List<Category> CATEGORIES = List.of(
            new Category(1, "Salário"),
            new Category(2, "Alimentação"),
            new Category(3, "Moradia"),
            new Category(4, "Transporte"),
            new Category(5, "Lazer")
    );

    private long nextTransactionId = 1;
    private long nextBudgetId = 1;

    public ConsoleMenu(TransactionRepository transactionRepository, BudgetRepository budgetRepository) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n===== MONEYWISE =====");
            System.out.println("1. Registrar transação");
            System.out.println("2. Listar transações");
            System.out.println("3. Ver saldo");
            System.out.println("4. Definir orçamento");
            System.out.println("5. Ver % de comprometimento");
            System.out.println("6. Projetar investimento");
            System.out.println("7. Sair");
            System.out.print("Escolha: ");

            int option = scanner.nextInt();
            scanner.nextLine();   // ⚠️ consome o Enter que sobrou (gotcha clássico!)
            System.out.print("\n");

            switch (option) {
                case 1: registerTransaction(); break;
                case 2: listTransactions();    break;
                case 3: showBalance();         break;
                case 4: defineBudget();        break;
                case 5: showCommitment();      break;
                case 6: projectSavings();      break;
                case 7: running = false;       break;
                default: System.out.println("Opção inválida!");
            }
        }
        System.out.println("Até logo!");
    }

    private void registerTransaction() {
        System.out.print("Tipo (1 - Receita, 2 - Despesa): ");
        int type = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Descrição: ");
        String description = scanner.nextLine();

        System.out.print("Valor: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        Category category = chooseCategory();
        if (category == null) return;

        TransactionType transactionType = type == 1 ? TransactionType.INCOME : TransactionType.EXPENSE;
        Transaction transaction = new Transaction(
                nextTransactionId++, transactionType, description, amount, LocalDate.now(), category);
        transactionRepository.add(transaction);
        System.out.println("Transação registrada com sucesso!");
    }

    private void listTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            System.out.println("Nenhuma transação registrada.");
            return;
        }
        for (Transaction t : transactions) {
            System.out.printf("%d | %s | %s | R$ %.2f | %s | %s%n",
                    t.getId(), t.getType(), t.getDescription(), t.getAmount(),
                    t.getDate(), t.getCategory().getName());
        }
    }

    private void showBalance() {
        BigDecimal balance = financialService.calculateBalance(transactionRepository.findAll());
        System.out.println("Saldo atual: R$ " + balance);
    }

    private void defineBudget() {
        Category category = chooseCategory();
        if (category == null) return;

        System.out.print("Mês (AAAA-MM): ");
        YearMonth month = YearMonth.parse(scanner.nextLine());

        System.out.print("Limite do orçamento: ");
        BigDecimal limit = scanner.nextBigDecimal();
        scanner.nextLine();

        Budget budget = new Budget(nextBudgetId++, category, month, limit);
        budgetRepository.add(budget);
        System.out.println("Orçamento definido!");
    }

    private void showCommitment() {
        Category category = chooseCategory();
        if (category == null) return;

        System.out.print("Mês (AAAA-MM): ");
        YearMonth month = YearMonth.parse(scanner.nextLine());

        Optional<Budget> budget = budgetRepository.findByCategoryAndMonth(category, month);
        if (budget.isEmpty()) {
            System.out.println("Nenhum orçamento definido para essa categoria/mês.");
            return;
        }
        BigDecimal percentage = budgetService.calculateCommitmentPercentage(
                transactionRepository.findAll(), budget.get());
        System.out.println("Comprometimento: " + percentage + "%");
    }

    private void projectSavings() {
        System.out.print("Capital inicial: ");
        BigDecimal capital = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("Taxa mensal (0.01 = 1%): ");
        BigDecimal rate = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("Meses: ");
        int months = scanner.nextInt();
        scanner.nextLine();

        BigDecimal result = savingsService.projectSavings(capital, rate, months);
        System.out.println("Projeção após " + months + " meses: R$ " + result);
    }

    private Category chooseCategory() {
        System.out.println("Categorias:");
        for (Category c : CATEGORIES) {
            System.out.println("  " + c.getId() + " - " + c.getName());
        }
        System.out.print("Escolha o id: ");
        long id = scanner.nextLong();
        scanner.nextLine();
        return CATEGORIES.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
}