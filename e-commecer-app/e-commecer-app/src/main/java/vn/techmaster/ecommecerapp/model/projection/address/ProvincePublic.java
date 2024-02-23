package vn.techmaster.ecommecerapp.model.projection.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import vn.techmaster.ecommecerapp.entity.Province;

public interface ProvincePublic {
    String getCode();
    String getName();
    String getNameEn();
    String getFullName();
    String getFullNameEn();
    String getCodeName();

    @RequiredArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
    class Delegator implements ProvincePublic {
        @Delegate(types = ProvincePublic.class)
        @JsonIgnore
        private final Province delegate;
    }

    static ProvincePublic of(Province delegate) {
        return Delegator.of(delegate);
    }
}
