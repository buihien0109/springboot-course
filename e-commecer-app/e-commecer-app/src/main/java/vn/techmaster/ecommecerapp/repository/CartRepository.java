package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_UserId(Long userId);
}