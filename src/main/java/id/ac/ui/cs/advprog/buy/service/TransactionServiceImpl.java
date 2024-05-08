package id.ac.ui.cs.advprog.buy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buy.model.Transaction;
import id.ac.ui.cs.advprog.buy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;
    private final String authUrl = "http://35.198.243.155/";

    @Override
    public Transaction create(Transaction transaction,String token) throws JsonProcessingException {
        transactionRepository.create(transaction);

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("id",transaction.getTransactionId());
        paymentRequest.put("paymentStatus", "WAITING_RESPONSE");
        paymentRequest.put("paymentAmount",transaction.getTotalPrice());
        paymentRequest.put("buyerUsername",transaction.getUsername());
        String jsonRequest = objectMapper.writeValueAsString(paymentRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",token);
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                authUrl + "/payment-request/create", HttpMethod.POST, entity, String.class);

        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        Iterator<Transaction> transactionIterator = transactionRepository.findAll();
        List<Transaction>  allTransaction = new ArrayList<>();
        transactionIterator.forEachRemaining(allTransaction::add);
        return allTransaction;
    }

    @Override
    public Transaction findById(String transactionId) {
        return transactionRepository.findById(transactionId);
    }

    @Override
    public List<Transaction> findByUsername(String username) {
        Iterator<Transaction> userTransaction = transactionRepository.findByUsername(username);
        List<Transaction> userTransactionList = new ArrayList<>();
        userTransaction.forEachRemaining(userTransactionList::add);
        return userTransactionList;
    }

    @Override
    public Transaction updateStatus(String transactionId, String status) {
        Transaction transaction = transactionRepository.findById(transactionId);
        transaction.setStatus(status);
        return transactionRepository.update(transaction);
    }
}
