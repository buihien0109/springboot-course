package com.example.demo;

import com.example.demo.repo.ImageRepository;
import com.example.demo.service.BlogCrawlerService;
import com.example.demo.service.ImageCrawlerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Objects;

@SpringBootTest
class DemoCrawlImageApplicationTests {
    private final String DOWNLOAD_PATH = System.getProperty("user.dir") + "/downloads";

    @Autowired
    private ImageCrawlerService imageCrawlerService;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private BlogCrawlerService blogCrawlerService;

    @Test
    void crawl_images() {
        imageCrawlerService.crawlAndDownloadImages("food", DOWNLOAD_PATH, 10);
    }

    @Test
    void get_files_count() {
        System.out.println(getFilesCount(DOWNLOAD_PATH));
    }

    @Test
    void delete_all_image() {
        imageRepository.deleteAll();
    }

    @Test
    void crawl_a_blog() {
        String url = "https://pasgo.vn/blog/cach-lam-suon-sot-chua-ngot-kieu-mien-bac-cuc-ngon-com-3019";
        blogCrawlerService.crawlAndSaveBlogPost(url);
    }

    private int getFilesCount(String path) {
        File file = new File(path);
        return Objects.requireNonNull(file.listFiles()).length;
    }
}
