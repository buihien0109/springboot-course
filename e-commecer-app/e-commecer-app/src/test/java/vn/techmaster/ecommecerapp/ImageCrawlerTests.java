package vn.techmaster.ecommecerapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.utils.crawl.ImageCrawler2Service;
import vn.techmaster.ecommecerapp.utils.crawl.ImageCrawlerService;

@SpringBootTest
public class ImageCrawlerTests {
    @Autowired
    private ImageCrawlerService imageCrawlerService;
    @Autowired
    private ImageCrawler2Service imageCrawler2Service;


    @Test
    void crawl_images() {
        try {
            imageCrawlerService.crawlAndDownloadImages("food?license=free");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void crawl_images2() {
        try {
            imageCrawler2Service.crawlAndDownloadImages("food?license=free");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
