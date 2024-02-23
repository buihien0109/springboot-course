package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.model.dto.UserNormalDto;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByRoles_NameIn(List<String> roles);

    long countByCreatedAtBetween(Date start, Date end);

    List<User> findByCreatedAtBetween(Date start, Date end);

    @Query(nativeQuery = true, name = "getAllAvailabelUsersNormalDtoByAdmin")
    List<UserNormalDto> getAllAvailabelUsersNormalDtoByAdmin();

    @Query(nativeQuery = true, name = "getAllAvailabelByRole")
    List<UserNormalDto> getAllAvailabelByRole(String role);

    @Query(nativeQuery = true, name = "getAllUsersNormalDtoInRangeTime")
    List<UserNormalDto> getAllUsersNormalDtoInRangeTime(Date start, Date end);
}