package id.ac.ui.cs.advprog.buy.model;

import id.ac.ui.cs.advprog.buy.enums.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    Transaction transaction1;
    Transaction transaction2;

    @BeforeEach
    void setUp(){
        String transactionId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        String username = "akun-1";
        String status = TransactionStatus.SUCCESS.getValue();
        String deliveryLocation = "Payakumbuh";
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        long price = 10000;

        this.transaction1 = new Transaction(transactionId,username,deliveryLocation,listings,price);
        this.transaction2 = new Transaction(transactionId,username,deliveryLocation,listings,price,status);
    }

    @Test
    void testGetTransactionId(){
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6",this.transaction1.getTransactionId());
    }

    @Test
    void testGetUsername(){
        assertEquals("akun-1",this.transaction1.getUsername());
    }

    @Test
    void testGetStatus(){
        assertEquals(TransactionStatus.WAITING_PAYMENT.getValue(), this.transaction1.getStatus());
        assertEquals(TransactionStatus.SUCCESS.getValue(),this.transaction2.getStatus());
    }

    @Test
    void testGetDeliveryLocation(){
        assertEquals("Payakumbuh",this.transaction1.getDeliveryLocation());
    }

    @Test
    void testGetPrice(){
        assertEquals(10000,this.transaction1.getTotalPrice());
    }

    @Test
    void testGetListings(){
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        assertEquals(listings, this.transaction1.getListings());
    }

    @Test
    void testUnknownStatus(){
        assertThrows(IllegalArgumentException.class, () ->
                this.transaction1.setStatus("LUNAS"));
    }

    @Test
    void testEmptyListingsTransaction(){
        String transactionId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        String username = "akun-1";
        String deliveryLocation = "Payakumbuh";
        Map<String,Integer> listings = new HashMap<>();
        long price = 10000;

        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(transactionId,username,deliveryLocation,listings,price));
    }
}
