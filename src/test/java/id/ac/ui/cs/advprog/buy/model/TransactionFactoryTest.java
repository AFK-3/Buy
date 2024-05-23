package id.ac.ui.cs.advprog.buy.model;

import id.ac.ui.cs.advprog.buy.enums.TransactionStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionFactoryTest {
    @Test
    void testCreateTransaction() {
        Cart cart = mock(Cart.class);

        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);

        when(cart.getUsername()).thenReturn("testUser");
        when(cart.getListings()).thenReturn(listings);
        when(cart.getTotalPrice()).thenReturn(10000L);

        Transaction transaction = TransactionFactory.createTransaction(cart, "123", "Jakarta");

        assertEquals("123", transaction.getTransactionId());
        assertEquals("testUser", transaction.getUsername());
        assertEquals("Jakarta", transaction.getDeliveryLocation());
        assertEquals(10000, transaction.getTotalPrice());
        assertEquals(transaction.getListings(),listings);
        assertEquals(transaction.getStatus(), TransactionStatus.WAITING_PAYMENT.getValue());
    }
}