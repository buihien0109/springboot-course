package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}