package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class TransactionRepository {
    private final List<Transaction> transactionData = new ArrayList<>();

    public Transaction create(Transaction transaction){
        transactionData.add(transaction);
        return transaction;
    }

    public Transaction update(Transaction updatedTransaction){
        for (Transaction transaction: transactionData){
            if (transaction.getTransactionId().equals(updatedTransaction.getTransactionId())){
                transaction.setStatus(updatedTransaction.getStatus());
                return transaction;
            }
        }
        throw new NoSuchElementException();
    }

    public Iterator<Transaction> findAll(){
        return transactionData.iterator();
    }

    public Transaction findById(String transactionId){
        for (Transaction transaction: transactionData){
            if (transaction.getTransactionId().equals(transactionId)){
                return transaction;
            }
        }
        throw new NoSuchElementException();
    }

    public Iterator<Transaction> findByUsername(String username){
        List<Transaction> userTransaction = new ArrayList<>();
        for (Transaction transaction: transactionData){
            if (transaction.getUsername().equals(username)){
                userTransaction.add(transaction);
            }
        }
        return userTransaction.iterator();
    }
}
