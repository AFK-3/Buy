package id.ac.ui.cs.advprog.buy.repository;

import id.ac.ui.cs.advprog.buy.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,String> {

}