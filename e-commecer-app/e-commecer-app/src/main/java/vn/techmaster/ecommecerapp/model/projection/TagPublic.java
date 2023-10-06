package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Tag;

public interface TagPublic {
    Integer getId();

    String getName();

    String getSlug();

    @RequiredArgsConstructor
    class TagPublicImpl implements TagPublic {
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
    }

    static TagPublic of(Tag tag) {
        return new TagPublicImpl(tag);
    }
}
