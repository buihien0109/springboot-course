package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.District;
import vn.techmaster.ecommecerapp.entity.Province;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.address.DistrictPublic;
import vn.techmaster.ecommecerapp.model.projection.address.ProvincePublic;
import vn.techmaster.ecommecerapp.model.projection.address.WardPublic;
import vn.techmaster.ecommecerapp.model.response.AddressInfo;
import vn.techmaster.ecommecerapp.repository.DistrictRepository;
import vn.techmaster.ecommecerapp.repository.ProvinceRepository;
import vn.techmaster.ecommecerapp.repository.WardRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressV2Service {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    public AddressInfo getAddress(Optional<String> provinceCode, Optional<String> districtCode, Optional<String> wardCode) {
        AddressInfo addressInfo = new AddressInfo();
        if (provinceCode.isPresent()) {
            ProvincePublic provincePublic = provinceRepository.findByCode(provinceCode.get())
                    .orElseThrow(() -> new ResouceNotFoundException("Not found province with code = " + provinceCode.get()));
            addressInfo.setProvince(provincePublic);
        }

        if (districtCode.isPresent()) {
            DistrictPublic districtPublic = districtRepository.findByCode(districtCode.get())
                    .orElseThrow(() -> new ResouceNotFoundException("Not found district with code = " + districtCode.get()));
            addressInfo.setDistrict(districtPublic);
        }

        if (wardCode.isPresent()) {
            WardPublic wardPublic = wardRepository.findByCode(wardCode.get())
                    .orElseThrow(() -> new ResouceNotFoundException("Not found ward with code = " + wardCode.get()));
            addressInfo.setWard(wardPublic);
        }

        return addressInfo;
    }

    public List<ProvincePublic> getProvinces() {
        return provinceRepository.findProvincePublicBy();
    }

    @Transactional
    public Province getProvinceByCode(String code) {
        return provinceRepository.getByCode(code)
                .orElseThrow(() -> new ResouceNotFoundException("Not found province with code = " + code));
    }

    public List<DistrictPublic> getDistricts(String provinceCode) {
        Province province = provinceRepository.findById(provinceCode)
                .orElseThrow(() -> new ResouceNotFoundException("Not found province with code = " + provinceCode));

        return districtRepository.findDistrictPublicByProvince_Code(provinceCode);
    }

    @Transactional
    public District getDistrictByCode(String code) {
        return districtRepository.getByCode(code)
                .orElseThrow(() -> new ResouceNotFoundException("Not found district with code = " + code));
    }

    public List<WardPublic> getWards(String districtCode) {
        District district = districtRepository.findById(districtCode)
                .orElseThrow(() -> new ResouceNotFoundException("Not found district with code = " + districtCode));

        return wardRepository.findWardPublicByDistrict_Code(districtCode);
    }
}
