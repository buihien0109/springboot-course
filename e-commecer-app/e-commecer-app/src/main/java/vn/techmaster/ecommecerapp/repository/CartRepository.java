package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}