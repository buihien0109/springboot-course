package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String linkRedirect;
    private String url;
    private Integer displayOrder;
    private Boolean status;

    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
    }
}
