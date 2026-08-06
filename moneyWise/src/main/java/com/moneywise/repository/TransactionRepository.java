package com.moneywise.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.moneywise.model.Transaction;

public class TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    public void add(Transaction transaction) {
        transactions.add(transaction);                    // armazena no final da lista
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);             // cópia defensiva (protege o estado interno)
    }

    public Optional<Transaction> findById(long id) {
        return transactions.stream()                       // "fluxo" sobre a lista
                .filter(t -> t.getId() == id)             // mantém só os que batem o id
                .findFirst();                             // pega o primeiro (ou vazio)
    }

    public boolean deleteById(long id) {
        return transactions.removeIf(t -> t.getId() == id); // remove e diz se removeu
    }
}