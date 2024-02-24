package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Ward;
import vn.techmaster.ecommecerapp.model.projection.address.WardPublic;

import java.util.List;
import java.util.Optional;

public interface WardRepository extends JpaRepository<Ward, String> {
    List<WardPublic> findWardPublicByDistrict_Code(String districtCode);

    Optional<WardPublic> findByCode(String wardCode);

    List<Ward> findByDistrict_Code(String districtCode);
}