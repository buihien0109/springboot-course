package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Role;

import java.nio.CharBuffer;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role);
}