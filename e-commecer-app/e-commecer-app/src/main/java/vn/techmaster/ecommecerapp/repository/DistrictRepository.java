package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.District;
import vn.techmaster.ecommecerapp.model.projection.address.DistrictPublic;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, String> {
    List<DistrictPublic> findDistrictPublicByProvince_Code(String provinceCode);

    Optional<DistrictPublic> findByCode(String provinceCode);

    Optional<District> getByCode(String code);

    List<District> findByProvince_Code(String provinceCode);
}