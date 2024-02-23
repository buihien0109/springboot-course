package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "token_confirm")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenConfirm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    Date createdDate;
    Date confirmedDate;
    Date expiryDate;

    public enum TokenType {
        EMAIL_VERIFICATION,
        PASSWORD_RESET
    }

    public TokenConfirm(String token, User user, TokenType tokenType, Date createdDate, Date expiryDate) {
        this.token = token;
        this.user = user;
        this.tokenType = tokenType;
        this.createdDate = createdDate;
        this.expiryDate = expiryDate;
    }

    @PrePersist
    public void prePersist() {
        this.createdDate = new Date();
    }
}
