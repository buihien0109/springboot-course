package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Tag;

public interface TagAdminPublic {
    Integer getId();

    String getName();

    String getSlug();

    Integer getUsed();

    @RequiredArgsConstructor
    class TagAdminPublicImpl implements TagAdminPublic {
        @JsonIgnore
        private final Tag tag;

        @Override
        public Integer getId() {
            return tag.getId();
        }

        @Override
        public String getName() {
            return tag.getName();
        }

        @Override
        public String getSlug() {
            return tag.getSlug();
        }

        @Override
        public Integer getUsed() {
            return tag.getBlogs().size();
        }
    }

    static TagAdminPublic of(Tag tag) {
        return new TagAdminPublicImpl(tag);
    }
}
