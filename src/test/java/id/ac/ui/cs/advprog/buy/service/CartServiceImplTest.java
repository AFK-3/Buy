package id.ac.ui.cs.advprog.buy.service;

import id.ac.ui.cs.advprog.buy.middleware.AuthMiddleware;
import id.ac.ui.cs.advprog.buy.model.Cart;
import id.ac.ui.cs.advprog.buy.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    @Mock
    CartRepository cartRepository;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    CartServiceImpl cartService;

    @Test
    void testCreateCart(){
        Cart cart= getCarts().getFirst();

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart savedCart = cartService.create(cart);
        assertEquals(cart,savedCart);
        verify(cartRepository,times(1)).save(any(Cart.class));
    }

    @Test
    void testFindAll(){
        List<Cart> cartList = getCarts();
        when(cartRepository.findAll()).thenReturn(cartList);

        List<Cart> savedCarts = cartService.findAll();
        assertEquals(cartList.size(),savedCarts.size());
        assertEquals(cartList.get(0),savedCarts.get(0));
        assertEquals(cartList.get(1),savedCarts.get(1));
        verify(cartRepository,times(1)).findAll();
    }

    @Test
    void testFindByUsername(){
        Cart cart = getCarts().getFirst();
        when(cartRepository.findById(any(String.class))).thenReturn(Optional.of(cart));

        Cart foundCart = cartService.findByUsername("1");
        assertEquals(cart,foundCart);
        verify(cartRepository,times(1)).findById(any(String.class));
    }

    @Test
    void testFindByInvalidUsername(){
        when(cartRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> cartService.findByUsername("1"));
        verify(cartRepository, times(1)).findById(any(String.class));
    }

    @Test
    void testAddListings(){
        Cart cart = getCarts().getFirst();

        Map<String,Integer> additionalListings = new HashMap<>();
        additionalListings.put("1",1);

        Map<String,Integer> listingAfter = new HashMap<>();
        listingAfter.put("1",4);
        listingAfter.put("2",4);
        Cart cartAfter = getCarts().getFirst();
        cartAfter.setListings(listingAfter);

        when(cartRepository.findById(any(String.class))).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart result = cartService.addListings(additionalListings,"1");
        assertEquals(result.getListings(),cartAfter.getListings());
        verify(cartRepository, times(1)).findById(any(String.class));
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testReduceListings(){
        Cart cart = getCarts().getFirst();

        Map<String,Integer> reducedListings = new HashMap<>();
        reducedListings.put("1",1);

        Map<String,Integer> listingAfter = new HashMap<>();
        listingAfter.put("1",2);
        listingAfter.put("2",4);
        Cart cartAfter = getCarts().getFirst();
        cartAfter.setListings(listingAfter);

        when(cartRepository.findById(any(String.class))).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart result = cartService.reduceListings(reducedListings,"1");
        assertEquals(result.getListings(),cartAfter.getListings());
        verify(cartRepository, times(1)).findById(any(String.class));
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testReduceListingsNewQuantityLessZero(){
        Cart cart = getCarts().getFirst();

        Map<String,Integer> reducedListings = new HashMap<>();
        reducedListings.put("1",4);

        Map<String,Integer> listingAfter = new HashMap<>();
        listingAfter.put("2",4);
        Cart cartAfter = getCarts().getFirst();
        cartAfter.setListings(listingAfter);

        when(cartRepository.findById(any(String.class))).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart result = cartService.reduceListings(reducedListings,"1");
        assertEquals(result.getListings(),cartAfter.getListings());
        verify(cartRepository, times(1)).findById(any(String.class));
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testDeleteListing(){
        Cart cart = getCarts().getFirst();

        Map<String,Integer> listingAfter = new HashMap<>();
        listingAfter.put("1",3);
        Cart cartAfter = getCarts().getFirst();
        cartAfter.setListings(listingAfter);

        when(cartRepository.findById(any(String.class))).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart result = cartService.deleteListing("2","1");
        assertEquals(result.getListings(),cartAfter.getListings());
        verify(cartRepository, times(1)).findById(any(String.class));
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testUpdateTotalPrice() throws JSONException {
        Cart cart = getCarts().getFirst();

        when(cartRepository.findById(any(String.class))).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        String responseBody = "{\"price\": 100}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","a");
        HttpEntity<String> entity = new HttpEntity<>("body",headers);

        System.out.println(entity);

        Mockito.when(restTemplate.exchange(AuthMiddleware.authUrl + "listing/get-by-id/1", HttpMethod.GET, entity, String.class)).thenReturn(responseEntity);
        Mockito.when(restTemplate.exchange(AuthMiddleware.authUrl + "listing/get-by-id/2", HttpMethod.GET, entity, String.class)).thenReturn(responseEntity);

        cartService.updateTotalPrice("1","a");

        assertEquals(cart.getTotalPrice(),700);
    }

    private static List<Cart> getCarts() {
        Cart cart1 = new Cart("1");
        Map<String,Integer> listings = new HashMap<>();
        listings.put("1",3);
        listings.put("2",4);
        cart1.setListings(listings);
        cart1.setTotalPrice(10000);

        Cart cart2 = new Cart("2");
        Map<String,Integer> listings2 = new HashMap<>();
        listings2.put("1",4);
        listings2.put("2",1);
        cart2.setListings(listings2);
        cart2.setTotalPrice(9000);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart1);
        cartList.add(cart2);
        return cartList;
    }


}
