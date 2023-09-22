package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

public interface CategoryCombineProductPublic {
    Long getCategoryId();

    String getName();

    String getSlug();

    List<ProductPublic> getProducts();

    @RequiredArgsConstructor
    class CategoryCombinePublicImpl implements CategoryCombineProductPublic {
        @JsonIgnore
        private final Category category;

        @Override
        public Long getCategoryId() {
            return category.getCategoryId();
        }

        @Override
        public String getName() {
            return category.getName();
        }

        @Override
        public String getSlug() {
            return category.getSlug();
        }

        @Override
        public List<ProductPublic> getProducts() {
            // if category has parent category, return list all products of subcategory
            // else return list all products of category
            if (category.getParentCategory() != null) {
                return category.getProducts().stream().map(ProductPublic::of).collect(Collectors.toList());
            } else {
                return category.getSubCategories().stream().flatMap(subCategory -> subCategory.getProducts().stream().map(ProductPublic::of)).collect(Collectors.toList());
            }
        }
    }

    static CategoryCombineProductPublic of(Category category) {
        return new CategoryCombinePublicImpl(category);
    }
}
