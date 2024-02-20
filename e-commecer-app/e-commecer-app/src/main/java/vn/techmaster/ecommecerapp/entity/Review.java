package vn.techmaster.ecommecerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.techmaster.ecommecerapp.model.dto.ReviewDto;

import java.util.Date;

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "ListReviewDtoResult",
                        classes = @ConstructorResult(
                                targetClass = ReviewDto.class,
                                columns = {
                                        @ColumnResult(name = "review_id", type = Long.class),
                                        @ColumnResult(name = "comment", type = String.class),
                                        @ColumnResult(name = "rating", type = Integer.class),
                                        @ColumnResult(name = "status", type = String.class),
                                        @ColumnResult(name = "author_id", type = Long.class),
                                        @ColumnResult(name = "author_name", type = String.class),
                                        @ColumnResult(name = "author_avatar", type = String.class),
                                        @ColumnResult(name = "created_at", type = String.class),
                                        @ColumnResult(name = "updated_at", type = String.class)
                                }
                        )
                )
        }
)

@NamedNativeQuery(
        name = "getAllReviewsAvailableByProductId",
        resultSetMapping = "ListReviewDtoResult",
        query = """
                SELECT r.review_id as review_id, r.comment as comment, r.rating as rating, r.status as status, u.user_id as author_id,
                COALESCE(u.username, r.author_name) as author_name, COALESCE(u.avatar, r.author_avatar) as author_avatar, r.created_at as created_at, r.updated_at as updated_at
                FROM review r
                LEFT JOIN user u ON r.user_id = u.user_id
                WHERE r.product_id = ?1 AND r.status = ?2
                ORDER BY r.created_at DESC
                """
)

@NamedNativeQuery(
        name = "getAllReviewsByProductId",
        resultSetMapping = "ListReviewDtoResult",
        query = """
                SELECT r.review_id as review_id, r.comment as comment, r.rating as rating, r.status as status, u.user_id as author_id,
                COALESCE(u.username, r.author_name) as author_name, COALESCE(u.avatar, r.author_avatar) as author_avatar, r.created_at as created_at, r.updated_at as updated_at
                FROM review r
                LEFT JOIN user u ON r.user_id = u.user_id
                WHERE r.product_id = ?1
                ORDER BY r.created_at DESC
                """
)

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String authorName;
    private String authorAvatar;
    private String authorEmail;
    private String authorPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
    }

    @Getter
    public enum Status {
        PENDING("Chờ kiểm duyệt"),
        ACCEPTED("Chấp nhận"),
        REJECTED("Từ chối");

        private final String displayValue;

        Status(String displayValue) {
            this.displayValue = displayValue;
        }
    }
}
