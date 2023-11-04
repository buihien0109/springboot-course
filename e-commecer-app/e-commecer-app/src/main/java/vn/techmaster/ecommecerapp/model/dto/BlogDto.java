package vn.techmaster.ecommecerapp.model.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.Blog}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogDto {
    private Integer id;
    private String title;
    private String slug;
    private String description;
    private String content;
    private String thumbnail;
    private Date createdAt;
    private Date updatedAt;
    private Date publishedAt;
    private Boolean status;
    private UserDto user;
    private List<TagDto> tags = new ArrayList<>();

    /**
     * DTO for {@link vn.techmaster.ecommecerapp.entity.User}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class UserDto {
        private Long userId;
        private String username;
        private String avatar;
    }

    /**
     * DTO for {@link vn.techmaster.ecommecerapp.entity.Tag}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class TagDto {
        private Integer id;
        private String name;
        private String slug;
    }
}