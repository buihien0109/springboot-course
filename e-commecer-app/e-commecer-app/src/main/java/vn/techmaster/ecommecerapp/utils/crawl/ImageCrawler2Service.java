package vn.techmaster.ecommecerapp.utils.crawl;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.repository.ProductRepository;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImageCrawler2Service {
    private final int MAX_SIZE_MB = 1;
    private final ProductRepository productRepository;

    public ImageCrawler2Service(ProductRepository productRepository) {
        this.productRepository = productRepository;
        WebDriverManager.chromedriver().setup();
    }


    // Crawl và lưu vào database
    @Transactional
    public void crawlAndDownloadImages(String keyword) throws InterruptedException {
        List<Product> products = productRepository.findByProductIdBetween(1L, 177L);

        WebDriver driver = new ChromeDriver();
        driver.get("https://unsplash.com/s/photos/" + keyword);


        // Cuộn trang để kích hoạt lazy loading
        int downloadedImageCount = 0;
        int maxImageCount = products.size() * 5;
        List<String> urls = new ArrayList<>();

        while (downloadedImageCount < maxImageCount) {
            // Lấy các phần tử hình ảnh
            List<WebElement> imageElements = driver.findElements(By.cssSelector("img.tB6UZ.a5VGX"));

            for (WebElement imageElement : imageElements) {
                String imageUrl = imageElement.getAttribute("src");

                // check image size
                if (imageUrl != null && !imageUrl.isEmpty() && isImageSizeValid(imageUrl, MAX_SIZE_MB)) {
                    if (!urls.contains(imageUrl)) {
                        urls.add(imageUrl);
                        downloadedImageCount++;
                    }
                }

                if (downloadedImageCount >= maxImageCount) {
                    break;
                }
            }

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(5000);
        }

        for (String url : urls) {
            insertImageForProduct(url);
        }

        driver.quit(); // Đóng trình duyệt khi hoàn thành
    }

    // Thêm ảnh cho product
    private void insertImageForProduct(String imageUrl) {
        log.info("Inserting image: " + imageUrl);
    }

    // Kiểm tra kích thước ảnh
    private boolean isImageSizeValid(String imageUrl, int maxSizeMB) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                int contentLength = httpConn.getContentLength();
                int maxSizeBytes = maxSizeMB * 1024 * 1024; // Convert maxSizeMB to bytes
                return contentLength <= maxSizeBytes;
            }
        } catch (IOException e) {
            log.error("Error checking image size: " + e.getMessage());
        }
        return false;
    }
}
