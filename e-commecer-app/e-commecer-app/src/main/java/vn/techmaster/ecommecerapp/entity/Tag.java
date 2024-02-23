package vn.techmaster.ecommecerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.techmaster.ecommecerapp.model.dto.TagUsedDto;

import java.util.ArrayList;
import java.util.List;

@SqlResultSetMapping(
        name = "TagUsedDtoResultMapping",
        classes = @ConstructorResult(
                targetClass = TagUsedDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "slug", type = String.class),
                        @ColumnResult(name = "used", type = Integer.class),
                }
        )
)

@NamedNativeQuery(
        name = "getAllTagsUsedDto",
        resultSetMapping = "TagUsedDtoResultMapping",
        query = """
                SELECT t.id, t.name, t.slug, COUNT(bt.blog_id) AS used
                FROM tag t
                LEFT JOIN blog_tag bt ON t.id = bt.tag_id
                GROUP BY t.id
                ORDER BY t.id DESC
                """
)

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tag")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "slug")
    String slug;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    List<Blog> blogs = new ArrayList<>();

    public Tag(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
