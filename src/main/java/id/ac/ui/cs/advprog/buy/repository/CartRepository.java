package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class CartRepository{
    private final List<Cart> cartData = new ArrayList<>();

    public Cart create(Cart cart){
        cartData.add(cart);
        return cart;
    }

    public Cart update(Cart updatedCart){
        for (Cart cart : cartData){
            if (cart.getUsername().equals(updatedCart.getUsername())){
                cart.setListings(updatedCart.getListings());
                cart.setTotalPrice(updatedCart.getTotalPrice());
                return cart;
            }
        }
        throw new NoSuchElementException();
    }

    public List<Cart> findAll(){
        return cartData;
    }

    public Cart findByUsername(String username){
        for (Cart cart: cartData){
            if (cart.getUsername().equals(username))
                return cart;
        }
        throw new NoSuchElementException();
    }
}
