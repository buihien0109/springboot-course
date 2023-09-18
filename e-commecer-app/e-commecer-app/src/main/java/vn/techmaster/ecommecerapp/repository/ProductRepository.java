package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}