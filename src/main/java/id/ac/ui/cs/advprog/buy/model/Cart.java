package id.ac.ui.cs.advprog.buy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @Entity @Table(name = "Cart")
public class Cart {
    @Id
    @Column(name = "username")
    private String username;

    @ElementCollection
    @CollectionTable(name = "listings", joinColumns = @JoinColumn(name = "username"))
    @MapKeyColumn(name = "listingId")
    @Column(name = "quantity")
    private Map<String, Integer> listings;

    @Column(name = "totalPrice")
    private long totalPrice;

    public Cart(String username){
        this.username = username;
    }

    public Cart() {
    }
}
