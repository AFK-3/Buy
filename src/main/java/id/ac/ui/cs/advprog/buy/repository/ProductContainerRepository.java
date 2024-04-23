package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.model.ProductContainer;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
abstract public class ProductContainerRepository {
    abstract ProductContainer create(String identifier);
    abstract ProductContainer filterByIdentifier(String identifier);
    public void addTo(ProductContainer productContainer, Map<String,Long> addListings){
        Map<String,Long> oldListings = productContainer.getListings();
        oldListings.putAll(addListings);
        productContainer.setListings(oldListings);
    }
}
