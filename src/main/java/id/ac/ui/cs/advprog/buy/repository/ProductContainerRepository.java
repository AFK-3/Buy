package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.model.ProductContainer;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
abstract public class ProductContainerRepository {
    abstract ProductContainer filterByUser(String username);
    public void addTo(ProductContainer productContainer, Map<String,Long> addListings){
        Map<String,Long> oldListings = productContainer.getListings();
        oldListings.putAll(addListings);
        productContainer.setListings(oldListings);
    }
    public long updateTotalPrice(ProductContainer productContainer,long newPrice){
        productContainer.setTotalPrice(newPrice);
        return newPrice;
    }
}
