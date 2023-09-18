package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}