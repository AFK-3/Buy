package id.ac.ui.cs.advprog.buy.controller;

import id.ac.ui.cs.advprog.buy.middleware.AuthMiddleware;
import id.ac.ui.cs.advprog.buy.model.Cart;
import id.ac.ui.cs.advprog.buy.model.Transaction;
import id.ac.ui.cs.advprog.buy.service.CartService;
import id.ac.ui.cs.advprog.buy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BuyControllerTest {
    @InjectMocks
    BuyController buyController;

    @Mock
    CartService cartService;

    @Mock
    TransactionService transactionService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(buyController).build();

    }

    @Test
    void tesAddToCart() throws Exception {
        Map<String, Integer> addListings = new HashMap<>();
        addListings.put("item1", 1);
        addListings.put("item2", 2);

        Cart cart = new Cart("username");
        cart.setListings(addListings);

        when(cartService.addListings(addListings ,"username")).thenReturn(cart);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(post("/cart")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"item1\":1,\"item2\":2}"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void tesAddToUncreatedCart() throws Exception {
        Map<String, Integer> addListings = new HashMap<>();
        addListings.put("item1", 1);
        addListings.put("item2", 2);

        Cart cart = new Cart("username");
        cart.setListings(addListings);

        when(cartService.addListings(addListings ,"username")).thenThrow(NoSuchElementException.class);
        when(cartService.create(any(Cart.class))).thenReturn(cart);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(post("/cart")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"item1\":1,\"item2\":2}"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void tesAddToCartNullUser() throws Exception {
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn(null);
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("BUYER");

            mockMvc.perform(post("/cart")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"item1\":1,\"item2\":2}"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void tesAddToCartNullRole() throws Exception {
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn(null);

            mockMvc.perform(post("/cart")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"item1\":1,\"item2\":2}"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void tesAddToCartForbiddenRole() throws Exception {
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("staff");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("STAFF");

            mockMvc.perform(post("/cart")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"item1\":1,\"item2\":2}"))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    void testReduceListingSuccess() throws Exception {
        Map<String, Integer> reduceListings = new HashMap<>();
        reduceListings.put("item1", 1);
        reduceListings.put("item2", 2);

        Cart cart = new Cart("username");
        cart.setListings(reduceListings);

        when(cartService.reduceListings(reduceListings, "username")).thenReturn(cart);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(put("/cart/reduce")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"item1\":1,\"item2\":2}"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testReduceListingCartNotFound() throws Exception {
        when(cartService.reduceListings(anyMap(), eq("username"))).thenThrow(NoSuchElementException.class);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(put("/cart/reduce")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"item1\":1,\"item2\":2}"))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void testReduceListingCartInvalidAuthentication() throws Exception {
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn(null);
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(put("/cart/reduce")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"item1\":1,\"item2\":2}"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void testAddListingSuccess() throws Exception {
        Map<String, Integer> addListing = new HashMap<>();
        addListing.put("item1", 1);
        addListing.put("item2", 2);

        Cart cart = new Cart("username");
        cart.setListings(addListing);

        when(cartService.addListings(addListing, "username")).thenReturn(cart);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(put("/cart/add")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"item1\":1,\"item2\":2}"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testAddListingCartNotFound() throws Exception {
        when(cartService.addListings(anyMap(), eq("username"))).thenThrow(NoSuchElementException.class);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(put("/cart/add")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"item1\":1,\"item2\":2}"))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void testAddListingCartInvalidAuthentication() throws Exception {
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn(null);
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(put("/cart/add")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"item1\":1,\"item2\":2}"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void testDeleteListingSuccess() throws Exception {
        Map<String, Integer> addListing = new HashMap<>();
        addListing.put("item2", 1);

        Cart cart = new Cart("username");
        cart.setListings(addListing);

        when(cartService.deleteListing("item1","username")).thenReturn(cart);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(delete("/cart")
                            .header("Authorization", "Bearer token")
                            .param("lstId", "item1"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testDeleteListingCartNotFound() throws Exception {
        when(cartService.deleteListing("item1","username")).thenThrow(NoSuchElementException.class);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(delete("/cart")
                            .header("Authorization", "Bearer token")
                            .param("lstId", "item1"))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void testDeleteListingCartInvalidAuthentication() throws Exception {
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn(null);
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(delete("/cart")
                            .header("Authorization", "Bearer token")
                            .param("lstId", "item1"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void testGetCartByUsernameSuccess() throws Exception {
        Cart cart = new Cart("username");

        when(cartService.findByUsername(anyString())).thenReturn(cart);

        mockMvc.perform(get("/cart/username")
                        .header("Authorization", "token-1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCartByUsernameNotFound() throws Exception {
        when(cartService.findByUsername(anyString())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/cart/username")
                        .header("Authorization", "token-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllCart() throws Exception {
        Cart cart1 = new Cart("username1");
        Cart cart2 = new Cart("username2");
        List<Cart> allCart = Arrays.asList(cart1, cart2);

        when(cartService.findAll()).thenReturn(allCart);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk());
    }

    @Test
    void testCheckoutSuccess() throws Exception {
        Cart cart = new Cart();
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3);
        listings.put("2",4);
        cart.setListings(listings);
        cart.setTotalPrice(10000);

        when(cartService.findByUsername(anyString())).thenReturn(cart);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(post("/transaction")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"deliveryLocation\":\"address\"}"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testCheckoutNotFound() throws Exception {
        when(cartService.findByUsername(anyString())).thenThrow(NoSuchElementException.class);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(post("/transaction")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"deliveryLocation\":\"address\"}"))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void testCheckoutInvalidAuthentication() throws Exception {
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn(null);
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(post("/transaction")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"deliveryLocation\":\"address\"}"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void testCheckoutInvalidDelivery() throws Exception {
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("Buyer");

            mockMvc.perform(post("/transaction")
                            .header("Authorization", "token-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"deliveryLocation\":null}"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void testUpdateTransactionStatusSuccess() throws Exception {
        Transaction transaction = new Transaction();
        when(transactionService.updateStatus(anyString(), anyString())).thenReturn(transaction);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("STAFF");

            mockMvc.perform(put("/transaction/transactionid")
                            .header("Authorization", "token-1")
                            .param("status", "SUCCESS"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testUpdateTransactionNotFound() throws Exception {
        when(transactionService.updateStatus(anyString(), anyString())).thenThrow(NoSuchElementException.class);
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("STAFF");

            mockMvc.perform(put("/transaction/transactionid")
                            .header("Authorization", "token-1")
                            .param("status", "SUCCESS"))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void testUpdateTransactionUnauthorized() throws Exception {
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn(null);

            mockMvc.perform(put("/transaction/transactionid")
                            .header("Authorization", "token-1")
                            .param("status", "SUCCESS"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void testUpdateTransactionForbidden() throws Exception {
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)){
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("BUYER");

            mockMvc.perform(put("/transaction/transactionid")
                            .header("Authorization", "token-1")
                            .param("status", "SUCCESS"))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    void testGetAllTransaction() throws Exception {
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> allTransaction = Arrays.asList(transaction1, transaction2);

        when(transactionService.findAll()).thenReturn(allTransaction);

        mockMvc.perform(get("/transaction"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTransactionByUser() throws Exception {
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> allTransaction = Arrays.asList(transaction1, transaction2);

        when(transactionService.findByUsername(anyString())).thenReturn(allTransaction);

        mockMvc.perform(get("/transaction/username")
                        .param("usr", "username"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTransactionByIdSuccess() throws Exception {
        Transaction transaction = new Transaction();
        when(transactionService.findById(anyString())).thenReturn(transaction);

        mockMvc.perform(get("/transaction/id")
                        .param("id", "transactionId"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTransactionByIdNotFound() throws Exception {
        when(transactionService.findById(anyString())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/transaction/id")
                        .param("id", "transactionId"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBuyPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Hello World!<h1>"));
    }
}
