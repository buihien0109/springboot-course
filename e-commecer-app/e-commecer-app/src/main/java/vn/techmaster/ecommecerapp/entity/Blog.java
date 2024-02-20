package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.techmaster.ecommecerapp.model.dto.BlogDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SqlResultSetMapping(
        name = "BlogDtoMapping",
        classes = {
                @ConstructorResult(
                        targetClass = BlogDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Integer.class),
                                @ColumnResult(name = "title", type = String.class),
                                @ColumnResult(name = "slug", type = String.class),
                                @ColumnResult(name = "description", type = String.class),
                                @ColumnResult(name = "content", type = String.class),
                                @ColumnResult(name = "thumbnail", type = String.class),
                                @ColumnResult(name = "createdAt", type = Date.class),
                                @ColumnResult(name = "updatedAt", type = Date.class),
                                @ColumnResult(name = "publishedAt", type = Date.class),
                                @ColumnResult(name = "status", type = Boolean.class),
                                @ColumnResult(name = "user", type = String.class),
                                @ColumnResult(name = "tags", type = String.class)
                        }
                )
        }
)

@NamedNativeQueries(
        value = {
                @NamedNativeQuery(
                        name = "findBlogDtoByIdAndSlugAndStatus",
                        query = "SELECT b.id, b.title, b.slug, b.description, b.content, b.thumbnail, " +
                                "b.created_at AS createdAt, b.updated_at AS updatedAt, b.published_at AS publishedAt, " +
                                "b.status, JSON_OBJECT('userId', u.user_id, 'username', u.username, 'avatar', u.avatar) AS user, " +
                                "JSON_ARRAYAGG(JSON_OBJECT('id', t.id, 'name', t.name, 'slug', t.slug)) AS tags " +
                                "FROM blog b " +
                                "INNER JOIN user u ON b.user_id = u.user_id " +
                                "LEFT JOIN blog_tag bt ON b.id = bt.blog_id " +
                                "LEFT JOIN tag t ON bt.tag_id = t.id " +
                                "WHERE b.id = ?1 and b.slug = ?2 and b.status = ?3",
                        resultSetMapping = "BlogDtoMapping"
                ),
                @NamedNativeQuery(
                        name = "getAllBlogAdmin",
                        query = "SELECT b.id, b.title, b.slug, b.description, b.content, b.thumbnail, " +
                                "b.created_at AS createdAt, b.updated_at AS updatedAt, b.published_at AS publishedAt, " +
                                "b.status, JSON_OBJECT('userId', u.user_id, 'username', u.username, 'avatar', u.avatar) AS user, " +
                                "JSON_ARRAYAGG(JSON_OBJECT('id', t.id, 'name', t.name, 'slug', t.slug)) AS tags " +
                                "FROM blog b " +
                                "INNER JOIN user u ON b.user_id = u.user_id " +
                                "LEFT JOIN blog_tag bt ON b.id = bt.blog_id " +
                                "LEFT JOIN tag t ON bt.tag_id = t.id " +
                                "GROUP BY b.id, b.created_at " +
                                "ORDER BY b.created_at DESC",
                        resultSetMapping = "BlogDtoMapping"
                ),
                @NamedNativeQuery(
                        name = "getAllOwnBlogAdmin",
                        query = "SELECT b.id, b.title, b.slug, b.description, b.content, b.thumbnail, " +
                                "b.created_at AS createdAt, b.updated_at AS updatedAt, b.published_at AS publishedAt, " +
                                "b.status, JSON_OBJECT('userId', u.user_id, 'username', u.username, 'avatar', u.avatar) AS user, " +
                                "JSON_ARRAYAGG(JSON_OBJECT('id', t.id, 'name', t.name, 'slug', t.slug)) AS tags " +
                                "FROM blog b " +
                                "INNER JOIN user u ON b.user_id = u.user_id " +
                                "LEFT JOIN blog_tag bt ON b.id = bt.blog_id " +
                                "LEFT JOIN tag t ON bt.tag_id = t.id " +
                                "WHERE u.user_id = ?1 " +
                                "GROUP BY b.id, b.created_at " +
                                "ORDER BY b.created_at DESC",
                        resultSetMapping = "BlogDtoMapping"
                ),
                @NamedNativeQuery(
                        name = "getBlogDtoById",
                        query = "SELECT b.id, b.title, b.slug, b.description, b.content, b.thumbnail, " +
                                "b.created_at AS createdAt, b.updated_at AS updatedAt, b.published_at AS publishedAt, " +
                                "b.status, JSON_OBJECT('userId', u.user_id, 'username', u.username, 'avatar', u.avatar) AS user, " +
                                "JSON_ARRAYAGG(JSON_OBJECT('id', t.id, 'name', t.name, 'slug', t.slug)) AS tags " +
                                "FROM blog b " +
                                "INNER JOIN user u ON b.user_id = u.user_id " +
                                "LEFT JOIN blog_tag bt ON b.id = bt.blog_id " +
                                "LEFT JOIN tag t ON bt.tag_id = t.id " +
                                "WHERE b.id = ?1",
                        resultSetMapping = "BlogDtoMapping"
                )
        }

)

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String thumbnail;
    private Date createdAt;
    private Date updatedAt;
    private Date publishedAt;
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "blog_tag",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
        updatedAt = createdAt;
        if (status) {
            publishedAt = createdAt;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
        if (status) {
            publishedAt = updatedAt;
        }
    }
}
