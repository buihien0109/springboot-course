package vn.techmaster.ecommecerapp.model.projection;

import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Role;
import vn.techmaster.ecommecerapp.entity.User;

import java.util.List;
import java.util.Set;

public interface UserPublic {
    Long getUserId();

    String getUsername();

    String getEmail();

    String getPhone();

    String getAvatar();

    Set<Role> getRoles();

    @RequiredArgsConstructor
    class UserPublicImpl implements UserPublic {
        private final User user;

        @Override
        public Long getUserId() {
            return this.user.getUserId();
        }

        @Override
        public String getUsername() {
            return this.user.getUsername();
        }

        @Override
        public String getEmail() {
            return this.user.getEmail();
        }

        @Override
        public String getPhone() {
            return this.user.getPhone();
        }

        @Override
        public String getAvatar() {
            return this.user.getAvatar();
        }

        @Override
        public Set<Role> getRoles() {
            return this.user.getRoles();
        }
    }

    static UserPublic of(User user) {
        return new UserPublicImpl(user);
    }
}
