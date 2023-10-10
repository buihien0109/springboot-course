package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "blog_tag",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

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
