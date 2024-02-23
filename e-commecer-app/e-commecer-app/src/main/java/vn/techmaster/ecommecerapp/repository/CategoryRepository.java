package vn.techmaster.ecommecerapp.repository;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.techmaster.ecommecerapp.entity.Category;
import vn.techmaster.ecommecerapp.model.dto.CategoryDto;
import vn.techmaster.ecommecerapp.model.dto.CategorySeparateDto;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentCategoryIsNull();

    List<Category> findByParentCategory_CategoryId(Long categoryId);

    List<Category> findByParentCategory_SlugIgnoreCase(String slug);

    Optional<Category> findBySlug(String categorySlug);

    @Query("SELECT new vn.techmaster.ecommecerapp.model.dto.CategoryDto(c.categoryId, c.name, c.slug, c.parentCategory.categoryId, c.parentCategory.name, c.parentCategory.slug) FROM Category c WHERE c.slug = ?1")
    Optional<CategoryDto> findCategoryDtoBySlug(String categorySlug);

    @Query("SELECT new vn.techmaster.ecommecerapp.model.dto.CategoryDto(c.categoryId, c.name, c.slug, c.parentCategory.categoryId, c.parentCategory.name, c.parentCategory.slug) FROM Category c WHERE c.parentCategory IS NOT NULL")
    List<CategoryDto> findAllSubCategory();

    List<Category> findByParentCategoryIsNotNull();

    @Query("SELECT new vn.techmaster.ecommecerapp.model.dto.CategoryDto(c.categoryId, c.name, c.slug, c.parentCategory.categoryId, c.parentCategory.name, c.parentCategory.slug) FROM Category c WHERE c.parentCategory.slug = ?1")
    List<CategoryDto> findAllSubCategoryByParentCategorySlug(String slug);

    @Query(nativeQuery = true, name = "getAllCategorySeparateDto")
    List<CategorySeparateDto> getAllCategorySeparateDto();

    @Query(nativeQuery = true, name = "getAllCategoryIsParent")
    List<CategoryDto> getAllCategoryIsParent();

    Optional<Category> findByName(String name);
}