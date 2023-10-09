package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.TokenConfirm;

import java.util.Optional;

public interface TokenConfirmRepository extends JpaRepository<TokenConfirm, Long> {
    Optional<TokenConfirm> findByToken(String token);

    Optional<TokenConfirm> findByTokenAndTokenType(String token, TokenConfirm.TokenType tokenType);
}