package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.enums.TransactionStatus;
import id.ac.ui.cs.advprog.buy.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {
    @Autowired
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
        transactionRepository.save(transaction);

        List<Transaction> transactionList = transactionRepository.findAll();
        assertEquals(transactionList.size(),1);

        Transaction savedTransaction = transactionList.getFirst();
        assertEquals(transaction.getTransactionId(),savedTransaction.getTransactionId());
    }

    @Test
    void testFindAllEmpty(){
        List<Transaction> transactionList = transactionRepository.findAll();
        assertEquals(transactionList.size(),0);
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
        transactionRepository.save(oldTransaction);

        Transaction newTransaction = new Transaction(transactionId,username,deliveryLocation,listings,price,TransactionStatus.SUCCESS.getValue());
        transactionRepository.save(newTransaction);

        List<Transaction> transactionList = transactionRepository.findAll();
        assertEquals(transactionList.size(),1);

        Transaction savedTransaction = transactionList.getFirst();
        assertEquals(newTransaction.getTransactionId(),savedTransaction.getTransactionId());
        assertEquals(newTransaction.getStatus(),savedTransaction.getStatus());
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
        transactionRepository.save(transaction);

        List<Transaction> userTransactions = transactionRepository.findByUsername("akun-1");
        assertEquals(userTransactions.size(),1);

        Transaction savedTransaction = userTransactions.getFirst();
        assertEquals(transaction.getTransactionId(),savedTransaction.getTransactionId());
    }

    @Test
    void testFindByInvalidUsername(){
        List<Transaction> transactionList = transactionRepository.findByUsername("akun-1");
        assertEquals(transactionList.size(),0);
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
        transactionRepository.save(transaction);

        Transaction savedTransaction = transactionRepository.findById(transactionId).get();
        assertEquals(savedTransaction.getTransactionId(),transaction.getTransactionId());
        assertEquals(savedTransaction.getUsername(), transaction.getUsername());
        assertEquals(savedTransaction.getDeliveryLocation(), transaction.getDeliveryLocation());
    }

    @Test
    void testFindByInvalidTransactionId(){
        assertNull(transactionRepository.findById("1").orElse(null));
    }
}
