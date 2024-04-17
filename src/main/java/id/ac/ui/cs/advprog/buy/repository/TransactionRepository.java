package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.model.ProductContainer;
import id.ac.ui.cs.advprog.buy.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class TransactionRepository extends ProductContainerRepository {
    private final Map<String, Transaction> transactionData = new HashMap<>();

    @Override
    public Transaction filterByUser(String username) {
        Optional<Transaction> transactionOptional = transactionData.values().stream()
                .filter(transaction -> transaction.getUsername().equals(username))
                .findFirst();

        return transactionOptional.orElse(null);
    }

    public Transaction filterById(String id){
        return transactionData.get(id);
    }
}
