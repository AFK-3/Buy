package id.ac.ui.cs.advprog.buy.controller;

import id.ac.ui.cs.advprog.buy.model.Cart;
import id.ac.ui.cs.advprog.buy.model.Transaction;
import id.ac.ui.cs.advprog.buy.model.TransactionFactory;
import id.ac.ui.cs.advprog.buy.repository.CartRepository;
import id.ac.ui.cs.advprog.buy.service.CartService;
import id.ac.ui.cs.advprog.buy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Cart> addToCart(@RequestParam("usr") String username, @RequestBody Map<String,Integer> addListings){
        try {
            Cart cart = cartService.addListings(addListings,username);
            // TODO : Jalankan service update price
            return ResponseEntity.ok(cart);
        } catch (NoSuchElementException e){
            Cart cart = new Cart(username);
            cart.setListings(addListings);
            cartService.create(cart);
            // TODO : Jalankan service update price
            return ResponseEntity.ok(cart);
        }
    }

    @PutMapping("/cart/reduce")
    public ResponseEntity<?> reduceListing(@RequestParam("usr") String username, @RequestBody Map<String,Integer> reduceListings){
        try{
            Cart updatedCart = cartService.reduceListings(reduceListings,username);
            // TODO : Jalankan update price
            return ResponseEntity.ok(updatedCart);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for user: " + username);
        }
    }

    @PutMapping("/cart/add")
    public ResponseEntity<?> addListing(@RequestParam("usr") String username, @RequestBody Map<String,Integer> addListings){
        try{
            Cart updatedCart = cartService.addListings(addListings,username);
            // TODO : Jalankan update price
            return ResponseEntity.ok(updatedCart);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for user: " + username);
        }
    }

    @DeleteMapping("/cart")
    public ResponseEntity<?> deleteListing(@RequestParam("usr")String username, @RequestParam("lstId") String listingId){
        try{
            Cart updatedCart = cartService.deleteListing(listingId,username);
            // TODO : Jalankan update price
            return ResponseEntity.ok(updatedCart);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for user: " + username);
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
    public ResponseEntity<?> checkout(@RequestBody Map<String,String> userData){
        String username = userData.get("username");
        String deliveryLocation = userData.get("deliveryLocation");

        if (username == null || deliveryLocation == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data");
        }

        try{
            Cart selectedCart = cartService.findByUsername(username);
            UUID uuid = UUID.randomUUID();

            Transaction transaction = TransactionFactory.createTransaction(selectedCart,uuid.toString(),deliveryLocation);
            transactionService.create(transaction);
            return ResponseEntity.ok(transaction);

        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for user: " + username);
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

    @GetMapping("/transaction/{username}")
    public ResponseEntity<List<Transaction>> getTransactionByUser(@PathVariable("username")String username){
        List<Transaction> allTransaction = transactionService.findByUsername(username);
        return ResponseEntity.ok(allTransaction);
    }

    @GetMapping("/cart")
    public ResponseEntity<?> getTransactionById(@RequestParam("id") String id){
        try{
            Transaction transaction = transactionService.findById(id);
            return ResponseEntity.ok(transaction);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found for id: " + id);
        }
    }



}
