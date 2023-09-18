package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}