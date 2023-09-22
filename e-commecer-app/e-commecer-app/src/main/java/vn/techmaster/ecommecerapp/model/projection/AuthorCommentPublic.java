package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.User;

public interface AuthorCommentPublic {
    Long getUserId();

    String getUsername();

    String getAvatar();

    @RequiredArgsConstructor
    class AuthorCommentPublicImpl implements AuthorCommentPublic {
        @JsonIgnore
        public final User user;

        @Override
        public Long getUserId() {
            return this.user.getUserId();
        }

        @Override
        public String getUsername() {
            return this.user.getUsername();
        }

        @Override
        public String getAvatar() {
            return this.user.getAvatar();
        }
    }

    static AuthorCommentPublic of(User user) {
        return new AuthorCommentPublicImpl(user);
    }
}
