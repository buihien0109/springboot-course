package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.entity.District;
import vn.techmaster.ecommecerapp.entity.Province;
import vn.techmaster.ecommecerapp.model.projection.address.DistrictPublic;
import vn.techmaster.ecommecerapp.model.projection.address.ProvincePublic;
import vn.techmaster.ecommecerapp.model.projection.address.WardPublic;
import vn.techmaster.ecommecerapp.service.AddressV2Service;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/public/address")
@RequiredArgsConstructor
public class AddressV2Resources {
    private final AddressV2Service addressService;

    @GetMapping
    public ResponseEntity<?> getAddress(@RequestParam Optional<String> provinceCode,
                                        @RequestParam Optional<String> districtCode,
                                        @RequestParam Optional<String> wardCode) {
        return ResponseEntity.ok(addressService.getAddress(provinceCode, districtCode, wardCode));
    }

    @GetMapping("/provinces")
    public List<ProvincePublic> getProvinces() {
        return addressService.getProvinces();
    }

    @GetMapping("/provinces/{code}")
    public Province getProvinceByCode(@PathVariable String code) {
        return addressService.getProvinceByCode(code);
    }

    @GetMapping("/districts")
    public List<DistrictPublic> getDistricts(@RequestParam String provinceCode) {
        return addressService.getDistricts(provinceCode);
    }

    @GetMapping("/districts/{code}")
    public District getDistrictByCode(@PathVariable String code) {
        return addressService.getDistrictByCode(code);
    }

    @GetMapping("/wards")
    public List<WardPublic> getWards(@RequestParam String districtCode) {
        return addressService.getWards(districtCode);
    }
}
