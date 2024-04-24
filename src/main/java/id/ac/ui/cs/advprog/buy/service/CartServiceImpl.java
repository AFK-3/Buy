package id.ac.ui.cs.advprog.buy.service;

import id.ac.ui.cs.advprog.buy.model.Cart;
import id.ac.ui.cs.advprog.buy.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;

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
    public Cart updateTotalPrice(long newPrice, String username) {
        // TODO : Hubungkan dengan microservice listings
        Cart cart = cartRepository.findByUsername(username);
        cart.setTotalPrice(newPrice);
        return cartRepository.update(cart);
    }
}
