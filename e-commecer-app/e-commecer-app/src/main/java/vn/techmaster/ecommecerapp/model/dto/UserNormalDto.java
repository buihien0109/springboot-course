package vn.techmaster.ecommecerapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.techmaster.ecommecerapp.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.User}
 */
@NoArgsConstructor
@Getter
@Setter
public class UserNormalDto implements Serializable {
    private Long userId;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private Date createdAt;

    public UserNormalDto(Long userId, String username, String email, String phone, String avatar, String createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.createdAt = DateUtils.parseDate(createdAt);
    }
}