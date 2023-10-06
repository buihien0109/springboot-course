package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Comment;

import java.time.LocalDateTime;

public interface CommentPublic {
    Integer getId();

    String getContent();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    AuthorCommentPublic getUser();

    @RequiredArgsConstructor
    class CommentPublicImpl implements CommentPublic {
        @JsonIgnore
        private final Comment comment;

        @Override
        public Integer getId() {
            return this.comment.getId();
        }

        @Override
        public String getContent() {
            return this.comment.getContent();
        }

        @Override
        public LocalDateTime getCreatedAt() {
            return this.comment.getCreatedAt();
        }

        @Override
        public LocalDateTime getUpdatedAt() {
            return this.comment.getUpdatedAt();
        }

        @Override
        public AuthorCommentPublic getUser() {
            return AuthorCommentPublic.of(comment.getUser());
        }
    }

    static CommentPublic of(Comment comment) {
        return new CommentPublicImpl(comment);
    }
}
