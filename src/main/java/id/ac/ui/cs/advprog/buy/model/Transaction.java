package id.ac.ui.cs.advprog.buy.model;

import id.ac.ui.cs.advprog.buy.enums.TransactionStatus;
import lombok.Getter;

import java.util.Map;

@Getter
public class Transaction {
    private String transactionId;
    private String username;
    private String status;
    private String deliveryLocation;
    private Map<String,Integer> listings;
    private long totalPrice;

    public Transaction(String transactionId, String username, String deliveryLocation, Map<String,Integer> listings, long totalPrice){
        this.transactionId = transactionId;
        this.username = username;
        this.deliveryLocation = deliveryLocation;
        if (listings.isEmpty()){
            throw new IllegalArgumentException();
        } else {
            this.listings = listings;
        }
        this.totalPrice = totalPrice;
    }

    public Transaction(String transactionId, String username, String deliveryLocation, Map<String,Integer> listings, long totalPrice,String status){
        this(transactionId, username, deliveryLocation, listings, totalPrice);
        this.setStatus(status);
    }

    public void setStatus(String status){
        if(TransactionStatus.contains(status)){
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
