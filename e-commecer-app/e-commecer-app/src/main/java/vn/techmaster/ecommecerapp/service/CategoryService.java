package vn.techmaster.ecommecerapp.service;

import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Category;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.CategoryPublic;
import vn.techmaster.ecommecerapp.model.projection.CategorySeparatePublic;
import vn.techmaster.ecommecerapp.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryPublic> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryPublic::of).toList();
    }

    public CategoryPublic findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find category by id " + id));
        return CategoryPublic.of(category);
    }

    public CategoryPublic save(Category category) {
        Category categorySaved = categoryRepository.save(category);
        return CategoryPublic.of(categorySaved);
    }

    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find category by id " + id));
        categoryRepository.deleteById(id);
    }

    public List<CategorySeparatePublic> findAllByParentCategoryIsNull() {
        List<Category> categories = categoryRepository.findByParentCategoryIsNull();
        return categories.stream().map(CategorySeparatePublic::of).toList();
    }

    public List<CategoryPublic> findAllByParentCategoryId(Long id) {
        List<Category> categories = categoryRepository.findByParentCategory_CategoryId(id);
        return categories.stream().map(CategoryPublic::of).toList();
    }

    // find all sub category by parent category name
    public List<CategoryPublic> findAllByParentCategoryName(String name) {
        List<Category> categories = categoryRepository.findByParentCategory_NameIgnoreCase(name);
        return categories.stream().map(CategoryPublic::of).toList();
    }

    // find all sub category by parent category slug
    public List<CategoryPublic> findAllByParentCategorySlug(String slug) {
        List<Category> categories = categoryRepository.findByParentCategory_SlugIgnoreCase(slug);
        return categories.stream().map(CategoryPublic::of).toList();
    }

    public CategoryPublic findBySlug(String categorySlug) {
        Category category = categoryRepository.findBySlug(categorySlug)
                .orElseThrow(() -> new ResouceNotFoundException("Cannot find category by slug " + categorySlug));
        return CategoryPublic.of(category);
    }
}
