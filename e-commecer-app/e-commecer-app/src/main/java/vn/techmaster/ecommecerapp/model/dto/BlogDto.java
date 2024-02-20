package vn.techmaster.ecommecerapp.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
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
    public static class TagDto {
        private Integer id;
        private String name;
        private String slug;
    }

    public BlogDto(Integer id, String title, String slug, String description, String content, String thumbnail, Date createdAt, Date updatedAt, Date publishedAt, Boolean status, String user, String tags) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.content = content;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.publishedAt = publishedAt;
        this.status = status;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.user = objectMapper.readValue((String) user, UserDto.class);
        } catch (IOException e) {
            this.user = null;
        }

        try {
            this.tags = objectMapper.readValue((String) tags, new TypeReference<List<TagDto>>() {});
        } catch (IOException e) {
            this.tags = new ArrayList<>();
        }
    }
}