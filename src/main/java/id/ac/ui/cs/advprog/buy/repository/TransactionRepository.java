package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.model.ProductContainer;
import id.ac.ui.cs.advprog.buy.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TransactionRepository extends ProductContainerRepository {
    private final Map<String, Transaction> transactionData = new HashMap<>();

    @Override
    public Transaction create(String identifier){
        Transaction newTransaction = new Transaction();
        transactionData.put(identifier,newTransaction);
        return newTransaction;
    }

    @Override
    public Transaction filterByIdentifier(String id){
        return transactionData.get(id);
    }

    public List<Transaction> filterByUser(String username){
        List<Transaction> filteredTransaction = new ArrayList<>();
        transactionData.forEach((key,value) -> {
            if (value.getUsername().equals(username)){
                filteredTransaction.add(value);
            }
        });
        return filteredTransaction;
    }
}
