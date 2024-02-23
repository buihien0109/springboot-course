package vn.techmaster.ecommecerapp.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.techmaster.ecommecerapp.model.projection.address.DistrictPublic;
import vn.techmaster.ecommecerapp.model.projection.address.ProvincePublic;
import vn.techmaster.ecommecerapp.model.projection.address.WardPublic;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressInfo {
    private ProvincePublic province;
    private DistrictPublic district;
    private WardPublic ward;
}
