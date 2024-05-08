package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.enums.TransactionStatus;
import id.ac.ui.cs.advprog.buy.model.Transaction;
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
class TransactionRepositoryTest {
    @InjectMocks
    TransactionRepository transactionRepository;

    @Test
    void testCreateAndFind(){
        String transactionId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        String username = "akun-1";
        String deliveryLocation = "Payakumbuh";
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        long price = 10000;

        Transaction transaction = new Transaction(transactionId,username,deliveryLocation,listings,price);
        transactionRepository.create(transaction);

        Iterator<Transaction> transactionIterator = transactionRepository.findAll();
        assertTrue(transactionIterator.hasNext());

        Transaction savedTransaction = transactionIterator.next();
        assertEquals(transaction.getTransactionId(),savedTransaction.getTransactionId());
        assertFalse(transactionIterator.hasNext());
    }

    @Test
    void testFindAllEmpty(){
        Iterator<Transaction> transactionIterator = transactionRepository.findAll();
        assertFalse(transactionIterator.hasNext());
    }

    @Test
    void testUpdateTransaction(){
        String transactionId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        String username = "akun-1";
        String deliveryLocation = "Payakumbuh";
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        long price = 10000;

        Transaction oldTransaction = new Transaction(transactionId,username,deliveryLocation,listings,price);
        transactionRepository.create(oldTransaction);

        Transaction newTransaction = new Transaction(transactionId,username,deliveryLocation,listings,price,TransactionStatus.SUCCESS.getValue());
        transactionRepository.update(newTransaction);

        Iterator<Transaction> transactionIterator = transactionRepository.findAll();
        assertTrue(transactionIterator.hasNext());

        Transaction savedTransaction = transactionIterator.next();
        assertEquals(newTransaction.getTransactionId(),savedTransaction.getTransactionId());
        assertEquals(newTransaction.getStatus(),savedTransaction.getStatus());
        assertFalse(transactionIterator.hasNext());
    }

    @Test
    void testUpdateTransactionInvalid(){
        String transactionId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        String username = "akun-1";
        String deliveryLocation = "Payakumbuh";
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        long price = 10000;

        Transaction newTransaction = new Transaction(transactionId,username,deliveryLocation,listings,price);
        assertThrows(NoSuchElementException.class, () -> transactionRepository.update(newTransaction));
    }

    @Test
    void testFindByUsername(){
        String transactionId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        String username = "akun-1";
        String deliveryLocation = "Payakumbuh";
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        long price = 10000;

        Transaction transaction = new Transaction(transactionId,username,deliveryLocation,listings,price);
        transactionRepository.create(transaction);

        Iterator<Transaction> userTransactions = transactionRepository.findByUsername("akun-1");
        assertTrue(userTransactions.hasNext());

        Transaction savedTransaction = userTransactions.next();
        assertEquals(transaction.getTransactionId(),savedTransaction.getTransactionId());
        assertFalse(userTransactions.hasNext());
    }

    @Test
    void testFindByInvalidUsername(){
        Iterator<Transaction> userTransactions = transactionRepository.findByUsername("a");
        assertFalse(userTransactions.hasNext());
    }

    @Test
    void testFindByTransactionId(){
        String transactionId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        String username = "akun-1";
        String deliveryLocation = "Payakumbuh";
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        long price = 10000;

        Transaction transaction = new Transaction(transactionId,username,deliveryLocation,listings,price);
        transactionRepository.create(transaction);

        Transaction savedTransaction = transactionRepository.findById(transactionId);
        assertEquals(savedTransaction.getTransactionId(),transaction.getTransactionId());
        assertEquals(savedTransaction.getUsername(), transaction.getUsername());
        assertEquals(savedTransaction.getDeliveryLocation(), transaction.getDeliveryLocation());
    }

    @Test
    void testFindByInvalidTransactionId(){
        assertThrows(NoSuchElementException.class, () -> transactionRepository.findById("1"));
    }
}
