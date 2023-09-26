package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.UserAddress;

public interface UserAddressPublic {
    Long getId();

    String getProvince();

    String getDistrict();

    String getWard();

    String getDetail();

    Boolean getIsDefault();

    @RequiredArgsConstructor
    class UserAddressPublicImpl implements UserAddressPublic {
        @JsonIgnore
        private final UserAddress userAddress;

        @Override
        public Long getId() {
            return userAddress.getId();
        }

        @Override
        public String getProvince() {
            return userAddress.getProvince();
        }

        @Override
        public String getDistrict() {
            return userAddress.getDistrict();
        }

        @Override
        public String getWard() {
            return userAddress.getWard();
        }

        @Override
        public String getDetail() {
            return userAddress.getDetail();
        }

        @Override
        public Boolean getIsDefault() {
            return userAddress.getIsDefault();
        }
    }

    static UserAddressPublic of(UserAddress userAddress) {
        return new UserAddressPublicImpl(userAddress);
    }
}
