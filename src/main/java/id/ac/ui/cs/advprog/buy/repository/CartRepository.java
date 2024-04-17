package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Repository
public class CartRepository extends ProductContainerRepository{
    private final Map<String, Cart> cartData = new HashMap<>();

    public Cart updateCart(Cart updatedCart, String username){
        cartData.put(username,updatedCart);
        return updatedCart;
    }

    @Override
    public Cart filterByUser(String username){
        return cartData.get(username);
    }

    public Cart clearCart(Cart deletedCart){
        Map<String,Long> deletedCartListings = deletedCart.getListings();
        deletedCartListings.clear();
        deletedCart.setListings(deletedCartListings);
        return deletedCart;
    }
}
