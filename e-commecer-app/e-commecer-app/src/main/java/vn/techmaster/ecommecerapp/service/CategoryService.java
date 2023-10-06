package vn.techmaster.ecommecerapp.service;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Category;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.CategoryPublic;
import vn.techmaster.ecommecerapp.model.projection.CategorySeparatePublic;
import vn.techmaster.ecommecerapp.model.request.UpsertParentCategory;
import vn.techmaster.ecommecerapp.model.request.UpsertSubCategory;
import vn.techmaster.ecommecerapp.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final Slugify slugify;

    public List<CategoryPublic> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryPublic::of).toList();
    }

    public CategoryPublic findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy category với id " + id));
        return CategoryPublic.of(category);
    }

    public CategoryPublic save(Category category) {
        Category categorySaved = categoryRepository.save(category);
        return CategoryPublic.of(categorySaved);
    }

    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy category với id " + id));

        if (category.getParentCategory() != null) {
            // check caterogy has product use it. if has, cannot delete. if not, delete
            if (!category.getProducts().isEmpty()) {
                throw new BadRequestException("Không thể xoá category này vì có sản phẩm đang sử dụng");
            } else {
                categoryRepository.deleteById(id);
            }
        } else {
            // get sub category of parent category
            List<Category> subCategories = categoryRepository.findByParentCategory_CategoryId(id);
            // check sub category has product use it. if has, cannot delete. if not, delete
            for (Category subCategory : subCategories) {
                if (!subCategory.getProducts().isEmpty()) {
                    throw new BadRequestException("Không thể xoá category này vì có sản phẩm đang sử dụng");
                }
            }

            // delete category parent and sub category
            categoryRepository.delete(category);
        }
    }

    public List<CategorySeparatePublic> findAllByParentCategoryIsNull() {
        List<Category> categories = categoryRepository.findByParentCategoryIsNull();
        return categories.stream().map(CategorySeparatePublic::of).toList();
    }

    public List<CategoryPublic> findAllParentCategory() {
        List<Category> categories = categoryRepository.findByParentCategoryIsNull();
        return categories.stream().map(CategoryPublic::of).toList();
    }

    public List<CategoryPublic> findAllSubCategory() {
        List<Category> categories = categoryRepository.findByParentCategoryIsNotNull();
        return categories.stream().map(CategoryPublic::of).toList();
    }

    // find all sub category by parent category slug
    public List<CategoryPublic> findAllByParentCategorySlug(String slug) {
        List<Category> categories = categoryRepository.findByParentCategory_SlugIgnoreCase(slug);
        return categories.stream().map(CategoryPublic::of).toList();
    }

    public CategoryPublic findBySlug(String categorySlug) {
        Category category = categoryRepository.findBySlug(categorySlug)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy category với slug " + categorySlug));
        return CategoryPublic.of(category);
    }

    public CategorySeparatePublic addParentCategory(UpsertParentCategory request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(slugify.slugify(request.getName()));
        categoryRepository.save(category);
        return CategorySeparatePublic.of(category);
    }

    public CategoryPublic addSubCategory(UpsertSubCategory request) {
        // find parent category
        Category parentCategory = categoryRepository.findById(request.getParentCategoryId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy category với id " + request.getParentCategoryId()));

        // create new sub category
        Category subCategory = new Category();
        subCategory.setName(request.getName());
        subCategory.setSlug(slugify.slugify(request.getName()));
        subCategory.setParentCategory(parentCategory);
        return save(subCategory);
    }

    public CategoryPublic updateParentCategory(Long id, UpsertParentCategory request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy category với id " + id));
        category.setName(request.getName());
        category.setSlug(slugify.slugify(request.getName()));
        return save(category);
    }

    public CategoryPublic updateSubCategory(Long id, UpsertSubCategory request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy category với id " + id));
        category.setName(request.getName());
        category.setSlug(slugify.slugify(request.getName()));

        // find parent category
        Category parentCategory = categoryRepository.findById(request.getParentCategoryId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy category với id " + request.getParentCategoryId()));
        category.setParentCategory(parentCategory);
        return save(category);
    }
}
