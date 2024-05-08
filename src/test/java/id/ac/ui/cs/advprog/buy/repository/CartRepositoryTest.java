package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.model.Cart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartRepositoryTest {
    @InjectMocks
    CartRepository cartRepository;

    @Test
    void testCreateAndFind(){
        Cart cart= new Cart("1");
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        cart.setListings(listings);
        cart.setTotalPrice(10000);
        cartRepository.create(cart);

        Iterator<Cart> cartIterator = cartRepository.findAll();
        assertTrue(cartIterator.hasNext());

        Cart savedCart = cartIterator.next();
        assertEquals(savedCart.getUsername(),cart.getUsername());
        assertEquals(savedCart.getListings(),cart.getListings());
        assertEquals(savedCart.getTotalPrice(),cart.getTotalPrice());
    }

    @Test
    void testFindAllifEmpty(){
        Iterator<Cart> cartIterator = cartRepository.findAll();
        assertFalse(cartIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOne(){
        Cart cart1= new Cart("1");
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        cart1.setListings(listings);
        cart1.setTotalPrice(10000);
        cartRepository.create(cart1);

        Cart cart2= new Cart("2");
        Map<String,Integer> listings2 = new HashMap<>();
        listings2.put("1",3); listings2.put("2",4);
        cart2.setListings(listings2);
        cart2.setTotalPrice(10000);
        cartRepository.create(cart2);

        Iterator<Cart> cartIterator = cartRepository.findAll();
        assertTrue(cartIterator.hasNext());
        Cart savedCart = cartIterator.next();
        assertEquals(cart1.getUsername(),savedCart.getUsername());
        savedCart = cartIterator.next();
        assertEquals(cart2.getUsername(),savedCart.getUsername());
        assertFalse(cartIterator.hasNext());
    }

    @Test
    void testUpdateCart(){
        Cart oldCart= new Cart("1");
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        oldCart.setListings(listings);
        oldCart.setTotalPrice(10000);
        cartRepository.create(oldCart);

        Cart newCart = new Cart("1");
        Map<String,Integer> listings2 = new HashMap<>();
        listings2.put("1",3); listings2.put("2",3);
        newCart.setListings(listings2);
        newCart.setTotalPrice(11000);
        cartRepository.update(newCart);

        Iterator<Cart> cartIterator = cartRepository.findAll();
        assertTrue(cartIterator.hasNext());
        Cart savedCart = cartIterator.next();
        assertEquals(newCart.getUsername(),savedCart.getUsername());
        assertEquals(newCart.getListings(), savedCart.getListings());
        assertEquals(newCart.getTotalPrice(),savedCart.getTotalPrice());
        assertFalse(cartIterator.hasNext());
    }

    @Test
    void testUpdateCartInvalid(){
        Cart otherUserCart = new Cart("2");
        Map<String,Integer> listings2 = new HashMap<>();
        listings2.put("1",3); listings2.put("2",3);
        otherUserCart.setListings(listings2);
        otherUserCart.setTotalPrice(11000);

        assertThrows(NoSuchElementException.class, () -> cartRepository.update(otherUserCart));
    }

    @Test
    void testFindbyUsername(){
        Cart cart= new Cart("2");
        Map<String,Integer> listing = new HashMap<>();
        listing.put("1",3); listing.put("2",4);
        cart.setListings(listing);
        cart.setTotalPrice(10000);
        cartRepository.create(cart);

        Cart savedCart = cartRepository.findByUsername("2");
        assertEquals(cart.getUsername(),savedCart.getUsername());
        assertEquals(cart.getListings(),savedCart.getListings());
        assertEquals(cart.getTotalPrice(),savedCart.getTotalPrice());
    }

    @Test
    void testFindInvalidUsername(){
        assertThrows(NoSuchElementException.class, () -> cartRepository.findByUsername("1"));
    }
}
