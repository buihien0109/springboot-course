package vn.techmaster.ecommecerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.techmaster.ecommecerapp.model.dto.CategoryDto;
import vn.techmaster.ecommecerapp.model.dto.CategorySeparateDto;

import java.util.ArrayList;
import java.util.List;

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "CategorySeparateDtoMapping",
                        classes = @ConstructorResult(
                                targetClass = CategorySeparateDto.class,
                                columns = {
                                        @ColumnResult(name = "main_category", type = String.class),
                                        @ColumnResult(name = "sub_categories", type = String.class)
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "CategoryDtoMapping",
                        classes = @ConstructorResult(
                                targetClass = CategoryDto.class,
                                columns = {
                                        @ColumnResult(name = "category_id", type = Long.class),
                                        @ColumnResult(name = "name", type = String.class),
                                        @ColumnResult(name = "slug", type = String.class),
                                        @ColumnResult(name = "parent_category_Id", type = Long.class),
                                        @ColumnResult(name = "parent_category_name", type = String.class),
                                        @ColumnResult(name = "parent_category_slug", type = String.class)
                                }
                        )
                )
        }
)

@NamedNativeQueries(
        value = {
                @NamedNativeQuery(
                        name = "getAllCategorySeparateDto",
                        resultSetMapping = "CategorySeparateDtoMapping",
                        query = """
                                SELECT
                                    JSON_OBJECT('categoryId', c.category_id, 'name', c.name, 'slug', c.slug) AS main_category,
                                    JSON_ARRAYAGG(JSON_OBJECT('categoryId', sc.category_id, 'name', sc.name, 'slug', sc.slug, 'parentCategoryId', c.category_id, 'parentCategoryName', c.name, 'parentCategorySlug', c.slug)) AS sub_categories
                                FROM
                                    category c
                                LEFT JOIN category sc ON
                                    c.category_id = sc.parent_category_id
                                WHERE
                                    c.parent_category_id IS NULL
                                GROUP BY
                                    c.category_id
                                """

                ),
                @NamedNativeQuery(
                        name = "getAllCategoryIsParent",
                        resultSetMapping = "CategoryDtoMapping",
                        query = """
                                SELECT
                                    c.category_id AS category_id,
                                    c.name AS name,
                                    c.slug AS slug,
                                    null AS parent_category_id,
                                    null AS parent_category_name,
                                    null AS parent_category_slug
                                FROM
                                    category c
                                WHERE
                                    c.parent_category_id IS NULL
                                """

                )
        }
)

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<Category> subCategories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<Product> products = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }
}
