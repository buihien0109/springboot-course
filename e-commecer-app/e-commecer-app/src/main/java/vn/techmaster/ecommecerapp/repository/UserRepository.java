package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.User;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByRoles_NameIn(List<String> roles);

    long countByCreatedAtBetween(Date start, Date end);

    List<User> findByCreatedAtBetween(Date start, Date end);
}