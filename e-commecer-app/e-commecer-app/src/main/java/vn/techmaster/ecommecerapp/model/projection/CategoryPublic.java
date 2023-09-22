package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Category;

public interface CategoryPublic {
    Long getCategoryId();

    String getName();

    String getSlug();

    Long getParentCategoryId();

    String getParentCategoryName();

    String getParentCategorySlug();

    @RequiredArgsConstructor
    class CategoryPublicImpl implements CategoryPublic {
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
        public Long getParentCategoryId() {
            if (category.getParentCategory() == null)
                return null;
            return category.getParentCategory().getCategoryId();
        }

        @Override
        public String getParentCategoryName() {
            if (category.getParentCategory() == null)
                return null;
            return category.getParentCategory().getName();
        }

        @Override
        public String getParentCategorySlug() {
            if (category.getParentCategory() == null) {
                return null;
            }
            return category.getParentCategory().getSlug();
        }
    }

    static CategoryPublic of(Category category) {
        return new CategoryPublicImpl(category);
    }
}
