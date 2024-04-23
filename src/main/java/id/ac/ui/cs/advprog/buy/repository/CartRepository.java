package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CartRepository extends ProductContainerRepository{
    private final Map<String, Cart> cartData = new HashMap<>();

    @Override
    public Cart create(String username){
        Cart newCart = new Cart();
        cartData.put(username,newCart);
        return newCart;
    }

    public Cart update(Cart updatedCart, String username){
        cartData.put(username,updatedCart);
        return updatedCart;
    }

    public Map<String,Cart> findAll(){
        return cartData;
    }

    @Override
    public Cart filterByIdentifier(String username){
        return cartData.get(username);
    }

    public Cart clearCart(Cart deletedCart){
        Map<String,Long> deletedCartListings = deletedCart.getListings();
        deletedCartListings.clear();
        deletedCart.setListings(deletedCartListings);
        return deletedCart;
    }
}
