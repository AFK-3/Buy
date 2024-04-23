package id.ac.ui.cs.advprog.buy.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Transaction extends ProductContainer{
    private String transactionId;
    private String username;
    private boolean status;
    private String deliveryLocation;
}
