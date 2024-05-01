package id.ac.ui.cs.advprog.buy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.ac.ui.cs.advprog.buy.model.Cart;
import id.ac.ui.cs.advprog.buy.model.Transaction;
import id.ac.ui.cs.advprog.buy.model.TransactionFactory;
import id.ac.ui.cs.advprog.buy.service.CartService;
import id.ac.ui.cs.advprog.buy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class BuyController {
    @Autowired
    private CartService cartService;

    @Autowired
    private TransactionService transactionService;
    @GetMapping("/")
    @ResponseBody
    public String buyPage(){
        return "<h1>Hello World!<h1>";
    }

    @PostMapping("/cart")
    public ResponseEntity<?> addToCart(@RequestParam("usr") String username,@RequestHeader("Authorization") String token, @RequestBody Map<String,Integer> addListings){
        Cart cart;
        try {
            cart = cartService.addListings(addListings,username);
        } catch (NoSuchElementException e){
            cart = new Cart(username);
            cart.setListings(addListings);
            cartService.create(cart);
        }

        try {
            cartService.updateTotalPrice(username,token);
            return ResponseEntity.ok(cart);
        } catch (JSONException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to parse JSON");
        }
    }

    @PutMapping("/cart/reduce")
    public ResponseEntity<?> reduceListing(@RequestParam("usr") String username, @RequestHeader("Authorization") String token, @RequestBody Map<String,Integer> reduceListings){
        try{
            Cart updatedCart = cartService.reduceListings(reduceListings,username);
            cartService.updateTotalPrice(username,token);
            return ResponseEntity.ok(updatedCart);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for user: " + username);
        } catch (JSONException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to parse JSON");
        }
    }

    @PutMapping("/cart/add")
    public ResponseEntity<?> addListing(@RequestParam("usr") String username, @RequestHeader("Authorization") String token, @RequestBody Map<String,Integer> addListings){
        try{
            Cart updatedCart = cartService.addListings(addListings,username);
            cartService.updateTotalPrice(username,token);
            return ResponseEntity.ok(updatedCart);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for user: " + username);
        } catch (JSONException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to parse JSON");
        }
    }

    @DeleteMapping("/cart")
    public ResponseEntity<?> deleteListing(@RequestParam("usr")String username, @RequestParam("lstId") String listingId, @RequestHeader("Authorization") String token){
        try{
            Cart updatedCart = cartService.deleteListing(listingId,username);
            cartService.updateTotalPrice(username,token);
            return ResponseEntity.ok(updatedCart);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for user: " + username);
        } catch (JSONException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to parse JSON");
        }
    }

    @GetMapping("/cart/{username}")
    public ResponseEntity<?> getCartByUsername(@PathVariable String username){
        try{
            Cart cart = cartService.findByUsername(username);
            return ResponseEntity.ok(cart);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for user: " + username);
        }
    }

    @GetMapping("/cart")
    public ResponseEntity<List<Cart>> getAllCart(){
        List<Cart> allCart = cartService.findAll();
        return ResponseEntity.ok(allCart);
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> checkout(@RequestBody Map<String,String> userData, @RequestHeader("Authorization") String token){
        String username = userData.get("username");
        String deliveryLocation = userData.get("deliveryLocation");

        if (username == null || deliveryLocation == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data");
        }

        try{
            Cart selectedCart = cartService.findByUsername(username);
            UUID uuid = UUID.randomUUID();

            Transaction transaction = TransactionFactory.createTransaction(selectedCart,uuid.toString(),deliveryLocation);
            transactionService.create(transaction,token);
            return ResponseEntity.ok(transaction);

        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for user: " + username);
        } catch (JsonProcessingException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create payment transaction");
        }
    }

    @PutMapping("/transaction/{transactionid}")
    public ResponseEntity<?> updateTransactionStatus(@PathVariable String transactionid, @RequestParam("status") String status){
        try{
            Transaction transaction = transactionService.updateStatus(transactionid,status);
            return ResponseEntity.ok(transaction);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found for id: " + transactionid);
        }
    }

    @GetMapping("/transaction")
    public ResponseEntity<List<Transaction>> getAllTransaction(){
        List<Transaction> allTransaction = transactionService.findAll();
        return ResponseEntity.ok(allTransaction);
    }

    @GetMapping("/transaction/username")
    public ResponseEntity<List<Transaction>> getTransactionByUser(@RequestParam("usr")String username){
        List<Transaction> allTransaction = transactionService.findByUsername(username);
        return ResponseEntity.ok(allTransaction);
    }

    @GetMapping("/transaction/id")
    public ResponseEntity<?> getTransactionById(@RequestParam("id") String id){
        try{
            Transaction transaction = transactionService.findById(id);
            return ResponseEntity.ok(transaction);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found for id: " + id);
        }
    }
}
