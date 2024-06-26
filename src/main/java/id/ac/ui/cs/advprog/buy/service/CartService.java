package id.ac.ui.cs.advprog.buy.service;

import id.ac.ui.cs.advprog.buy.model.Cart;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.List;
import java.util.Map;

public interface CartService {
    public Cart create(Cart cart);
    public List<Cart> findAll();
    public Cart findByUsername(String username);
    public Cart addListings(Map<String,Integer> additionalListings, String username);
    public Cart reduceListings(Map<String,Integer> reducedListings, String username);
    public Cart deleteListing(String listingId, String username);
    public void updateTotalPrice(String username, String token) throws JSONException;

}
