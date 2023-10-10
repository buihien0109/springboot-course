package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Banner;

import java.util.Date;

public interface BannerPublic {
    Integer getId();

    String getName();

    String getLinkRedirect();

    String getUrl();

    Integer getDisplayOrder();

    Boolean getStatus();

    Date getCreatedAt();

    Date getUpdatedAt();

    @RequiredArgsConstructor
    class BannerPublicImpl implements BannerPublic {
        @JsonIgnore
        private final Banner banner;

        @Override
        public Integer getId() {
            return banner.getId();
        }

        @Override
        public String getName() {
            return banner.getName();
        }

        @Override
        public String getLinkRedirect() {
            return banner.getLinkRedirect();
        }

        @Override
        public String getUrl() {
            return banner.getUrl();
        }

        @Override
        public Integer getDisplayOrder() {
            return banner.getDisplayOrder();
        }

        @Override
        public Boolean getStatus() {
            return banner.getStatus();
        }

        @Override
        public Date getCreatedAt() {
            return banner.getCreatedAt();
        }

        @Override
        public Date getUpdatedAt() {
            return banner.getUpdatedAt();
        }
    }

    static BannerPublic of(Banner banner) {
        return new BannerPublicImpl(banner);
    }
}
