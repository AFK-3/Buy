package id.ac.ui.cs.advprog.buy.service;

import id.ac.ui.cs.advprog.buy.model.Transaction;

import java.util.List;

public interface TransactionService {
    public Transaction create(Transaction transaction);
    public Transaction findById(String transactionId);
    public List<Transaction> findByUsername(String username);
    public List<Transaction> findAll();
    public Transaction updateStatus(String transactionId, String status);
}
