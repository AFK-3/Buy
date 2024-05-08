package id.ac.ui.cs.advprog.buy.service;

import id.ac.ui.cs.advprog.buy.model.Cart;
import id.ac.ui.cs.advprog.buy.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;
    private final String authUrl = "http://35.198.243.155/";

    @Override
    public Cart create(Cart cart) {
        cartRepository.create(cart);
        return cart;
    }

    @Override
    public List<Cart> findAll() {
        Iterator<Cart> cartIterator = cartRepository.findAll();
        List<Cart> allCart = new ArrayList<>();
        cartIterator.forEachRemaining(allCart::add);
        return allCart;
    }

    @Override
    public Cart findByUsername(String username) {
        return cartRepository.findByUsername(username);
    }

    @Override
    public Cart addListings(Map<String, Integer> additionalListings, String username) {
        Cart cart = cartRepository.findByUsername(username);

        Map<String, Integer> currentListings = cart.getListings();

        for (Map.Entry<String, Integer> entry : additionalListings.entrySet()) {
            currentListings.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }

        cart.setListings(currentListings);
        return cartRepository.update(cart);
    }

    @Override
    public Cart reduceListings(Map<String, Integer> reducedListings, String username) {
        Cart cart = cartRepository.findByUsername(username);
        Map<String, Integer> currentListings = cart.getListings();

        for (Map.Entry<String, Integer> entry : reducedListings.entrySet()) {
            String productId = entry.getKey();
            Integer quantityToReduce = entry.getValue();
            Integer currentQuantity = currentListings.get(productId);

            int newQuantity = currentQuantity - quantityToReduce;
            if (newQuantity <= 0) {
                currentListings.remove(productId);
            } else {
                currentListings.put(productId, newQuantity);
            }

        }

        cart.setListings(currentListings);
        return cartRepository.update(cart);
    }

    @Override
    public Cart deleteListing(String listingId, String username) {
        Cart cart = cartRepository.findByUsername(username);
        Map<String,Integer> currentListings = cart.getListings();
        currentListings.remove(listingId);
        cart.setListings(currentListings);
        return cartRepository.update(cart);
    }

    @Override
    @Async
    public void updateTotalPrice(String username, String token) throws JSONException {
        Cart cart = cartRepository.findByUsername(username);
        Map<String,Integer> listings = cart.getListings();

        String getListingIdUrl = authUrl + "listing/" + "get-by-id/";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",token);
        HttpEntity<String> entity = new HttpEntity<>("body",headers);
        String jsonBody;
        JSONObject jsonObject;

        long totalPrice = 0;
        for (Map.Entry<String, Integer> entry : listings.entrySet()){
            String listingId = entry.getKey();
            Integer quantity = entry.getValue();

            ResponseEntity<String> response = restTemplate.exchange(getListingIdUrl + listingId, HttpMethod.GET,
                    entity, String.class);

            jsonBody = response.getBody();
            jsonObject = new JSONObject(jsonBody);
            Long price = jsonObject.getLong("price");

            totalPrice = totalPrice + (price * quantity);
        }

        cart.setTotalPrice(totalPrice);
        cartRepository.update(cart);
    }
}
