package id.ac.ui.cs.advprog.buy.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class Cart {
    private String username;
    private Map<String, Integer> listings;
    private long totalPrice;

    public Cart(String username){
        this.username = username;
    }
}
