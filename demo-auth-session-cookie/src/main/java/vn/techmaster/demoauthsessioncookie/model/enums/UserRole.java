package vn.techmaster.demoauthsessioncookie.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    String role;
}
