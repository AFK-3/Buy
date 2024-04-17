package id.ac.ui.cs.advprog.buy.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
abstract public class ProductContainer {
    private Map<String, Long> listings;
    private long totalPrice;
}
