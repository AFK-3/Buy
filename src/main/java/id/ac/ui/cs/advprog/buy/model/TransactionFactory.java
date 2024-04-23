package id.ac.ui.cs.advprog.buy.model;

import java.util.HashMap;

public class TransactionFactory {
    public static Transaction createTransaction(Cart cart, String transactionId, String status, String deliveryLocation){
        return new Transaction(
                transactionId,
                cart.getUsername(),
                deliveryLocation,
                new HashMap<>(cart.getListings()),
                cart.getTotalPrice(),
                status
        );
    }
}
