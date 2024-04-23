package id.ac.ui.cs.advprog.buy.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class Cart {
    private String username;
    private HashMap<String, Integer> listings;
    private long totalPrice;

    public Cart(String username){
        this.username = username;
    }
}
