package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.techmaster.ecommecerapp.model.dto.UserNormalDto;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "UserNormalDtoResult",
                        classes = @ConstructorResult(
                                targetClass = UserNormalDto.class,
                                columns = {
                                        @ColumnResult(name = "user_id", type = Long.class),
                                        @ColumnResult(name = "username", type = String.class),
                                        @ColumnResult(name = "email", type = String.class),
                                        @ColumnResult(name = "phone", type = String.class),
                                        @ColumnResult(name = "avatar", type = String.class),
                                        @ColumnResult(name = "created_at", type = String.class)
                                }
                        )
                )
        }
)

@NamedNativeQuery(
        name = "getAllAvailabelUsersNormalDtoByAdmin",
        resultSetMapping = "UserNormalDtoResult",
        query = """
                SELECT u.user_id as user_id, u.username as username, u.email as email, u.phone as phone, u.avatar as avatar, u.created_at as created_at
                FROM user u
                WHERE u.enabled = true
                ORDER BY u.created_at DESC
                """
)

@NamedNativeQuery(
        name = "getAllUsersNormalDtoInRangeTime",
        resultSetMapping = "UserNormalDtoResult",
        query = """
                SELECT u.user_id as user_id, u.username as username, u.email as email, u.phone as phone, u.avatar as avatar, u.created_at as created_at
                FROM user u
                WHERE u.created_at BETWEEN ?1 AND ?2
                ORDER BY u.created_at DESC
                """
)

@NamedNativeQuery(
        name = "getAllAvailabelByRole",
        resultSetMapping = "UserNormalDtoResult",
        query = """
                SELECT u.user_id as user_id, u.username as username, u.email as email, u.phone as phone, u.avatar as avatar, u.created_at as created_at
                FROM user u
                JOIN user_role ur ON u.user_id = ur.user_id
                JOIN role r ON ur.role_id = r.role_id
                WHERE r.name = ?1 AND u.enabled = true
                ORDER BY u.created_at DESC
                """
)

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    String username;

    @Column(nullable = false, unique = true)
    String email;

    String phone;

    @Column(nullable = false)
    String password;

    String avatar;
    Boolean enabled;
    Date createdAt;

    @ManyToMany(fetch = FetchType.EAGER) //EAGER: load hết dữ liệu của user và role
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new LinkedHashSet<>();

    public User(String username, String email, String password, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }
}

