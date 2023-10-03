package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Discount;

import java.util.Date;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

}