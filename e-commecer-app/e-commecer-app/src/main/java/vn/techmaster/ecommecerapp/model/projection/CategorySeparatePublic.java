package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Category;

import java.util.List;

public interface CategorySeparatePublic {
    CategoryPublic getMainCategory();

    List<CategoryPublic> getSubCategories();

    @RequiredArgsConstructor
    class CategorySeparatePublicImpl implements CategorySeparatePublic {
        @JsonIgnore
        private final Category category;

        @Override
        public CategoryPublic getMainCategory() {
            return CategoryPublic.of(category);
        }

        @Override
        public List<CategoryPublic> getSubCategories() {
            return category.getSubCategories().stream().map(CategoryPublic::of).toList();
        }
    }

    static CategorySeparatePublic of(Category category) {
        return new CategorySeparatePublicImpl(category);
    }
}
