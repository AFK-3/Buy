package id.ac.ui.cs.advprog.buy.model;

import id.ac.ui.cs.advprog.buy.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Map;

@Getter @Entity @Table(name = "transaction")
public class Transaction {
    @Id
    @Column(name = "transactionId")
    private String transactionId;

    @Column(name = "username")
    private String username;

    @Column(name = "status")
    private String status;

    @Column(name = "deliveryLocation")
    private String deliveryLocation;

    @ElementCollection
    @CollectionTable(name = "listingsTransaction", joinColumns = @JoinColumn(name = "transactionId"))
    @MapKeyColumn(name = "listingId")
    @Column(name = "quantity")
    private Map<String,Integer> listings;

    @Column(name = "totalPrice")
    private long totalPrice;

    public Transaction(String transactionId, String username, String deliveryLocation, Map<String,Integer> listings, long totalPrice){
        this.transactionId = transactionId;
        this.username = username;
        this.deliveryLocation = deliveryLocation;
        this.status = TransactionStatus.WAITING_PAYMENT.getValue();

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

    public Transaction() {
    }

    public void setStatus(String status){
        if(TransactionStatus.contains(status)){
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
