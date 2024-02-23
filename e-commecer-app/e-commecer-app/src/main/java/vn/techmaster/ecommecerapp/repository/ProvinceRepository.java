package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Province;
import vn.techmaster.ecommecerapp.model.projection.address.ProvincePublic;

import java.util.List;
import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<Province, String> {
    List<ProvincePublic> findProvincePublicBy();

    Optional<ProvincePublic> findByCode(String districtCode);

    Optional<Province> getByCode(String code);
}