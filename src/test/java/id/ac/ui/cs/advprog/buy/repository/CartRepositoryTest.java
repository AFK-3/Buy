package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.model.Cart;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class CartRepositoryTest {
    @Autowired
    CartRepository cartRepository;

    @Test
    void testCreateAndFind(){
        Cart cart= new Cart("1");
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        cart.setListings(listings);
        cart.setTotalPrice(10000);
        cartRepository.save(cart);

        List<Cart> cartList = cartRepository.findAll();
        assertEquals(cartList.size(),1);

        Cart savedCart = cartList.getFirst();
        assertEquals(savedCart.getUsername(),cart.getUsername());
        assertEquals(savedCart.getListings(),cart.getListings());
        assertEquals(savedCart.getTotalPrice(),cart.getTotalPrice());
    }

    @Test
    void testFindAllIfEmpty(){
        List<Cart> cartList = cartRepository.findAll();
        assertEquals(cartList.size(),0);
    }

    @Test
    void testFindAllIfMoreThanOne(){
        Cart cart1= new Cart("1");
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        cart1.setListings(listings);
        cart1.setTotalPrice(10000);
        cartRepository.save(cart1);

        Cart cart2= new Cart("2");
        Map<String,Integer> listings2 = new HashMap<>();
        listings2.put("1",3); listings2.put("2",4);
        cart2.setListings(listings2);
        cart2.setTotalPrice(10000);
        cartRepository.save(cart2);

        List<Cart> cartList = cartRepository.findAll();
        assertEquals(cartList.size(),2);

        Cart savedCart = cartList.getFirst();
        assertEquals(cart1.getUsername(),savedCart.getUsername());
        savedCart = cartList.get(1);
        assertEquals(cart2.getUsername(),savedCart.getUsername());
    }

    @Test
    void testUpdateCart(){
        Cart oldCart= new Cart("1");
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        oldCart.setListings(listings);
        oldCart.setTotalPrice(10000);
        cartRepository.save(oldCart);

        Cart newCart = new Cart("1");
        Map<String,Integer> listings2 = new HashMap<>();
        listings2.put("1",3); listings2.put("2",3);
        newCart.setListings(listings2);
        newCart.setTotalPrice(11000);
        cartRepository.save(newCart);

        List<Cart> cartList = cartRepository.findAll();
        assertEquals(cartList.size(),1);

        Cart savedCart = cartList.getFirst();
        assertEquals(newCart.getUsername(),savedCart.getUsername());
        assertEquals(newCart.getListings(), savedCart.getListings());
        assertEquals(newCart.getTotalPrice(),savedCart.getTotalPrice());
    }

    @Test
    void testFindByUsername(){
        Cart cart= new Cart("2");
        Map<String,Integer> listing = new HashMap<>();
        listing.put("1",3); listing.put("2",4);
        cart.setListings(listing);
        cart.setTotalPrice(10000);
        cartRepository.save(cart);

        Cart savedCart = cartRepository.findById("2").get();
        assertEquals(cart.getUsername(),savedCart.getUsername());
        assertEquals(cart.getListings(),savedCart.getListings());
        assertEquals(cart.getTotalPrice(),savedCart.getTotalPrice());
    }

    @Test
    void testFindInvalidUsername(){
        assertNull(cartRepository.findById("1").orElse(null));
    }
}
