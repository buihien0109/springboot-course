package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.entity.UserAddress;
import vn.techmaster.ecommecerapp.model.projection.UserAddressPublic;
import vn.techmaster.ecommecerapp.model.request.UpsertUserAddressRequest;
import vn.techmaster.ecommecerapp.repository.UserAddressRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressService {
    private final UserAddressRepository userAddressRepository;

    // find all address by user id
    public List<UserAddress> findAllAddressByUserId(Long userId) {
        return userAddressRepository.findByUser_UserId(userId);
    }

    // find all address by user login
    public List<UserAddressPublic> findAllAddressByUserLogin() {
        User user = SecurityUtils.getCurrentUserLogin();
        if (user != null) {
            List<UserAddress> addressList = userAddressRepository.findByUser_UserId(user.getUserId());
            // sort address list by isDefault true in first
            return addressList.stream()
                    .sorted((a1, a2) -> a2.getIsDefault().compareTo(a1.getIsDefault()))
                    .map(UserAddressPublic::of)
                    .toList();

        } else {
            return new ArrayList<>();
        }
    }

    // create new user address by param UpsertUserAddressRequest
    public UserAddressPublic createNewUserAddress(UpsertUserAddressRequest request) {
        if (request.getIsDefault()) {
            // set all address isDefault false
            List<UserAddress> addressList = userAddressRepository.findByUser_UserId(SecurityUtils.getCurrentUserLogin().getUserId());
            addressList.forEach(address -> address.setIsDefault(false));
            userAddressRepository.saveAll(addressList);
        }

        // create new user address
        UserAddress userAddress = new UserAddress();
        userAddress.setProvince(request.getProvince());
        userAddress.setDistrict(request.getDistrict());
        userAddress.setWard(request.getWard());
        userAddress.setDetail(request.getDetail());
        userAddress.setIsDefault(request.getIsDefault());
        userAddress.setUser(SecurityUtils.getCurrentUserLogin());
        userAddressRepository.save(userAddress);

        return UserAddressPublic.of(userAddress);
    }

    // update user address by param UpsertUserAddressRequest
    public UserAddressPublic updateUserAddress(Long id, UpsertUserAddressRequest request) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User address not found"));

        if (request.getIsDefault()) {
            // set all address isDefault false
            List<UserAddress> addressList = userAddressRepository.findByUser_UserId(SecurityUtils.getCurrentUserLogin().getUserId());
            addressList.forEach(address -> address.setIsDefault(false));
            userAddressRepository.saveAll(addressList);
        }

        // update user address
        userAddress.setProvince(request.getProvince());
        userAddress.setDistrict(request.getDistrict());
        userAddress.setWard(request.getWard());
        userAddress.setDetail(request.getDetail());
        userAddress.setIsDefault(request.getIsDefault());

        userAddressRepository.save(userAddress);
        return UserAddressPublic.of(userAddress);
    }

    // delete user address by id
    public void deleteUserAddressById(Long id) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User address not found"));
        userAddressRepository.deleteById(id);
    }

    public UserAddressPublic setDefaultUserAddress(Long id) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User address not found"));

        // set all address isDefault false
        List<UserAddress> addressList = userAddressRepository.findByUser_UserId(userAddress.getUser().getUserId());
        addressList.forEach(address -> address.setIsDefault(false));
        userAddressRepository.saveAll(addressList);

        userAddress.setIsDefault(true);
        userAddressRepository.save(userAddress);
        return UserAddressPublic.of(userAddress);
    }
}
