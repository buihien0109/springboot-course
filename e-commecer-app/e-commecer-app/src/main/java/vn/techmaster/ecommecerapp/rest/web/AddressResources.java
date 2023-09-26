package vn.techmaster.ecommecerapp.rest.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.techmaster.ecommecerapp.service.AddressService;

@RestController
@RequestMapping("/api/v1/address")
public class AddressResources {

    private final AddressService addressService;

    public AddressResources(AddressService addressService) {
        this.addressService = addressService;
    }

    // get all province
    @GetMapping("/provinces")
    public ResponseEntity<?> getAllProvince() throws JsonProcessingException {
        return ResponseEntity.ok(addressService.getAllProvince());
    }

    // get all district by province
    @GetMapping("/districts")
    public ResponseEntity<?> getAllDistrictByProvince(@RequestParam Integer province_id) throws JsonProcessingException {
        return ResponseEntity.ok(addressService.getAllDistrictByProvince(province_id));
    }

    // get all ward by district
    @GetMapping("/wards")
    public ResponseEntity<?> getAllWardByDistrict(@RequestParam Integer district_id) throws JsonProcessingException {
        return ResponseEntity.ok(addressService.getAllWardByDistrict(district_id));
    }
}
