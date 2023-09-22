package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_image")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    public static enum ImageType {
        MAIN,
        SUB
    }
}

