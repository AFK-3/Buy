package id.ac.ui.cs.advprog.buy.service;

import id.ac.ui.cs.advprog.buy.model.Transaction;
import id.ac.ui.cs.advprog.buy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction create(Transaction transaction) {
        transactionRepository.create(transaction);
        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        Iterator<Transaction> transactionIterator = transactionRepository.findAll();
        List<Transaction>  allTransaction = new ArrayList<>();
        transactionIterator.forEachRemaining(allTransaction::add);
        return allTransaction;
    }

    @Override
    public Transaction findById(String transactionId) {
        return transactionRepository.findById(transactionId);
    }

    @Override
    public List<Transaction> findByUsername(String username) {
        Iterator<Transaction> userTransaction = transactionRepository.findByUsername(username);
        List<Transaction> userTransactionList = new ArrayList<>();
        userTransaction.forEachRemaining(userTransactionList::add);
        return userTransactionList;
    }

    @Override
    public Transaction updateStatus(String transactionId, String status) {
        Transaction transaction = transactionRepository.findById(transactionId);
        transaction.setStatus(status);
        return transactionRepository.update(transaction);
    }
}
