package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}