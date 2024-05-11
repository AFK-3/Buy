package id.ac.ui.cs.advprog.buy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buy.enums.TransactionStatus;
import id.ac.ui.cs.advprog.buy.model.Transaction;
import id.ac.ui.cs.advprog.buy.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
    @Mock
    TransactionRepository transactionRepository;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    TransactionServiceImpl transactionService;

    @Test
    void testCreate() throws JsonProcessingException {
        Transaction transaction = getTransactions().getFirst();

        when(transactionRepository.save(transaction)).thenReturn(transaction);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("id",transaction.getTransactionId());
        paymentRequest.put("paymentStatus", "WAITING_RESPONSE");
        paymentRequest.put("paymentAmount",transaction.getTotalPrice());
        paymentRequest.put("buyerUsername",transaction.getUsername());
        String jsonRequest = objectMapper.writeValueAsString(paymentRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","token-1");
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);
        when(restTemplate.exchange(
                "http://35.198.243.155/"+ "/payment-request/create", HttpMethod.POST, entity, String.class)).thenReturn(new ResponseEntity<>("body", HttpStatus.OK));

        Transaction savedTransaction = transactionService.create(transaction,"token-1");
        assertEquals(transaction.getTransactionId(),savedTransaction.getTransactionId());
        verify(transactionRepository,times(1)).save(transaction);
        verify(restTemplate, times(1)).exchange("http://35.198.243.155/"+ "/payment-request/create", HttpMethod.POST, entity, String.class);
    }

    @Test
    void testFindAll(){
        List<Transaction> transactionList = getTransactions();
        when(transactionRepository.findAll()).thenReturn(transactionList);

        List<Transaction> savedTransactions = transactionService.findAll();
        assertEquals(transactionList.size(),savedTransactions.size());
        assertEquals(transactionList.get(0),savedTransactions.get(0));
        assertEquals(transactionList.get(1),savedTransactions.get(1));
        verify(transactionRepository,times(1)).findAll();
    }

    @Test
    void testFindById(){
        Transaction transaction = getTransactions().getFirst();
        when(transactionRepository.findById(any(String.class))).thenReturn(Optional.of(transaction));

        Transaction foundTransaction = transactionService.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");
        assertEquals(transaction,foundTransaction);
        verify(transactionRepository,times(1)).findById(any(String.class));
    }

    @Test
    void testFindByInvalidId(){
        when(transactionRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> transactionService.findById("1"));
        verify(transactionRepository, times(1)).findById(any(String.class));
    }

    @Test
    void testFindByUsername(){
        Transaction transaction = getTransactions().getFirst();
        List<Transaction> transactionList = new ArrayList<>(); transactionList.add(transaction);
        when(transactionRepository.findByUsername("akun-1")).thenReturn(transactionList);

        List<Transaction> savedTransactions = transactionService.findByUsername("akun-1");
        assertEquals(transactionList.size(),savedTransactions.size());
        assertEquals(transactionList.getFirst(),savedTransactions.getFirst());
        verify(transactionRepository,times(1)).findByUsername("akun-1");
    }

    @Test
    void testUpdateStatus(){
        Transaction transaction = getTransactions().getFirst();
        when(transactionRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6")).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction updatedTransaction = transactionService.updateStatus("eb558e9f-1c39-460e-8860-71af6af63bd6", TransactionStatus.SUCCESS.getValue());
        assertEquals(updatedTransaction.getStatus(),TransactionStatus.SUCCESS.getValue());
        verify(transactionRepository,times(1)).findById("eb558e9f-1c39-460e-8860-71af6af63bd6");
        verify(transactionRepository,times(1)).save(transaction);
    }

    private static List<Transaction> getTransactions() {
        String transactionId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        String username = "akun-1";
        String deliveryLocation = "Payakumbuh";
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3); listings.put("2",4);
        long price = 10000;
        Transaction transaction = new Transaction(transactionId,username,deliveryLocation,listings,price);

        String transactionId2 = "eb558e9f-1c39-460e-8860-71af6af63bd7";
        String username2 = "akun-2";
        String deliveryLocation2 = "Padang";
        Map<String,Integer> listings2 = new HashMap<>();
        listings2.put("1",3); listings2.put("2",4);
        long price2 = 10000;
        Transaction transaction2 = new Transaction(transactionId2,username2,deliveryLocation2,listings2,price2);

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        transactionList.add(transaction2);
        return transactionList;
    }
}
