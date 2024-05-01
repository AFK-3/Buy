package id.ac.ui.cs.advprog.buy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {
    Cart cart;

    @BeforeEach
    void setUp(){
        this.cart = new Cart("akun-1");

        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        this.cart.setListings(listings);
        this.cart.setTotalPrice(10000);
    }

    @Test
    void testGetCartUsername(){
        assertEquals("akun-1",this.cart.getUsername());
    }

    @Test
    void testGetCartListings(){
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        assertEquals(listings, this.cart.getListings());
    }

    @Test
    void testGetCartPrice(){
        assertEquals(10000,this.cart.getTotalPrice());
    }
}
