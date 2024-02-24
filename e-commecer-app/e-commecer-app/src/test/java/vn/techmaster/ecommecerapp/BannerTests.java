package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.Banner;
import vn.techmaster.ecommecerapp.entity.PaymentVoucher;
import vn.techmaster.ecommecerapp.repository.BannerRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class BannerTests {

    @Autowired
    private BannerRepository bannerRepository;

    @Test
    void save_banners() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        int displayOrder = 1;
        // save 10 banners and random status
        // if status = true -> display order = 1 and auto increment display order
        // if status = false -> display order = null
        for (int i = 0; i < 10; i++) {
            Banner banner = new Banner();
            String name = faker.book().title();
            banner.setName(name);
            banner.setSlug(slugify.slugify(name));
            banner.setLinkRedirect(faker.internet().url());
            banner.setUrl(generateLinkImage(name));
            banner.setStatus(faker.bool().bool());
            if (banner.getStatus()) {
                banner.setDisplayOrder(displayOrder++);
            }
            bannerRepository.save(banner);
        }
    }

    @Test
    void delete_all_banner() {
        bannerRepository.deleteAll();
    }

    @Test
    void update_created_at_banner() {
        List<Banner> bannerList = bannerRepository.findAll();
        Date start = new Calendar.Builder().setDate(2023, 11, 20).build().getTime();
        Date end = new Date();
        for (Banner banner : bannerList) {
            banner.setCreatedAt(randomDateBetweenTwoDates(start, end));
            bannerRepository.save(banner);
        }
    }

    // write method to random date between 2 date
    private Date randomDateBetweenTwoDates(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);
        return new Date(randomMillisSinceEpoch);
    }

    // get character first each of word from string, and to uppercase
    private String getCharacter(String str) {
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(word.charAt(0));
        }
        if (result.length() >= 2) {
            return result.substring(0, 2).toUpperCase();
        } else {
            return result.toString().toUpperCase();
        }
    }

    // generate link author avatar follow struct : https://placehold.co/200x200?text=[...]
    private String generateLinkImage(String authorName) {
        return "https://placehold.co/1000x600?text=" + getCharacter(authorName);
    }
}
