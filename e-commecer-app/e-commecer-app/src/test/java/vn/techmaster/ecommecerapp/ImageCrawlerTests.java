package vn.techmaster.ecommecerapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.utils.crawl.ImageCrawlerService;
import vn.techmaster.ecommecerapp.utils.crawl.ImageCrawlerServiceWinMart;

@SpringBootTest
public class ImageCrawlerTests {
    @Autowired
    private ImageCrawlerService imageCrawlerService;
    @Autowired
    private ImageCrawlerServiceWinMart imageCrawlerServiceWinMart;


    @Test
    void crawl_images() {
        try {
            imageCrawlerService.crawlAndDownloadImages("food?license=free");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void crawl_images_insert_image() {
        imageCrawlerService.insertImageForProduct();
    }

    @Test
    void crawl_a_product() {
        imageCrawlerServiceWinMart.crawlAndDownloadImages("https://www.bachhoaxanh.com/rau-sach/hanh-la-goi-100g", 145L);
    }

    @Test
    void demo_openWeb() {
        imageCrawlerServiceWinMart.openWeb();
    }
}
