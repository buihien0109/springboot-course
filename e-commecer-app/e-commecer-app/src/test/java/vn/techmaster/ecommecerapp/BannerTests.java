package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.Banner;
import vn.techmaster.ecommecerapp.repository.BannerRepository;

@SpringBootTest
public class BannerTests {

    @Autowired
    private BannerRepository bannerRepository;

    @Test
    void save_banners() {
        Faker faker = new Faker();
        int displayOrder = 1;
        // save 10 banners and random status
        // if status = true -> display order = 1 and auto increment display order
        // if status = false -> display order = null
        for (int i = 0; i < 10; i++) {
            Banner banner = new Banner();
            banner.setName(faker.commerce().productName());
            banner.setLinkRedirect(faker.internet().url());
            banner.setUrl(faker.company().logo());
            banner.setStatus(faker.bool().bool());
            if (banner.getStatus()) {
                banner.setDisplayOrder(displayOrder++);
            }
            bannerRepository.save(banner);
        }

    }
}
