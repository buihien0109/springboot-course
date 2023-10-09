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
@Table(name = "token_confirm")
public class TokenConfirm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private Date createdDate;
    private Date confirmedDate;
    private Date expiryDate;

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
