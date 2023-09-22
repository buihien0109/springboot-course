package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentCategoryIsNull();

    List<Category> findByParentCategory_CategoryId(Long categoryId);

    // find all sub category by parent category name
    List<Category> findByParentCategory_NameIgnoreCase(String name);

    List<Category> findByParentCategory_SlugIgnoreCase(String slug);

    Optional<Category> findBySlug(String categorySlug);
}