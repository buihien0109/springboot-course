package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Blog;

import java.util.Date;
import java.util.List;

public interface BlogPublic {
    Integer getId();

    String getTitle();

    String getSlug();

    String getDescription();

    String getContent();

    String getThumbnail();

    Date getCreatedAt(); // dd/MM/yyyy

    Date getUpdatedAt(); // dd/MM/yyyy

    Date getPublishedAt(); // dd/MM/yyyy

    Boolean getStatus();

    List<TagPublic> getTags();

    UserPublic getUser();

    @RequiredArgsConstructor
    class BlogPublicImpl implements BlogPublic {
        @JsonIgnore
        private final Blog blog;

        @Override
        public Integer getId() {
            return this.blog.getId();
        }

        @Override
        public String getTitle() {
            return this.blog.getTitle();
        }

        @Override
        public String getSlug() {
            return this.blog.getSlug();
        }

        @Override
        public String getDescription() {
            return this.blog.getDescription();
        }

        @Override
        public String getContent() {
            return this.blog.getContent();
        }

        @Override
        public String getThumbnail() {
            return this.blog.getThumbnail();
        }

        @Override
        public Date getCreatedAt() {
            return this.blog.getCreatedAt();
        }

        @Override
        public Date getUpdatedAt() {
            return this.blog.getUpdatedAt();
        }

        @Override
        public Date getPublishedAt() {
            return this.blog.getPublishedAt();
        }

        @Override
        public Boolean getStatus() {
            return this.blog.getStatus();
        }

        @Override
        public List<TagPublic> getTags() {
            return this.blog.getTags().stream()
                    .map(TagPublic::of)
                    .toList();
        }

        @Override
        public UserPublic getUser() {
            return UserPublic.of(this.blog.getUser());
        }

    }

    static BlogPublic of(Blog blog) {
        return new BlogPublicImpl(blog);
    }
}
