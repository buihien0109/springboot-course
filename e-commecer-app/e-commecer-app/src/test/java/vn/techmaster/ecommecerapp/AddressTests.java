package vn.techmaster.ecommecerapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.entity.UserAddress;
import vn.techmaster.ecommecerapp.repository.UserAddressRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;
import vn.techmaster.ecommecerapp.service.AddressService;

@SpringBootTest
public class AddressTests {

    @Autowired
    private AddressService addressService;
    @Autowired
    private UserAddressRepository userAddressRepository;
    @Autowired
    private UserRepository userRepository;

    // test get all province
    @Test
    public void testGetAllProvince() throws JsonProcessingException {
        System.out.println(addressService.getAllProvince());
    }

    // save 3 address for user : 1 default address and 2 other address
    @Test
    public void testSaveAddress() {
        User user = userRepository.findByEmail("user@gmail.com").get();
        UserAddress address1 = new UserAddress();
        address1.setProvince("Hà Nội");
        address1.setDistrict("Quận Hà Đông");
        address1.setWard("Phường Dương Nội");
        address1.setDetail("Số 1, ngõ 1, đường 1");
        address1.setIsDefault(true);
        address1.setUser(user);

        UserAddress address2 = new UserAddress();
        address2.setProvince("Hà Nội");
        address2.setDistrict("Quận Nam Từ Liêm");
        address2.setWard("Phường Trung Văn");
        address2.setDetail("28/132A Phố Đại Linh");
        address2.setIsDefault(false);
        address2.setUser(user);

        UserAddress address3 = new UserAddress();
        address3.setProvince("Hà Nội");
        address3.setDistrict("Quận Thanh Xuân");
        address3.setWard("Phường Hạ Đình");
        address3.setDetail("Số 3, ngõ 3, đường 3");
        address3.setIsDefault(false);
        address3.setUser(user);

        userAddressRepository.save(address1);
        userAddressRepository.save(address2);
        userAddressRepository.save(address3);
    }
}
