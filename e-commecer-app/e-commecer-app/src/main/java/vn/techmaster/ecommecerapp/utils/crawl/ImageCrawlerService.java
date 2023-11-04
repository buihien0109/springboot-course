package vn.techmaster.ecommecerapp.utils.crawl;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.FileServer;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.ProductImage;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.repository.FileServerRepository;
import vn.techmaster.ecommecerapp.repository.ProductImageRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImageCrawlerService {
    private final int MAX_SIZE_MB = 1;
    private final UserRepository userRepository;
    private final FileServerRepository fileServerRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;


    // Khởi tạo trình duyệt Chrome
    public ImageCrawlerService(UserRepository userRepository, FileServerRepository fileServerRepository, ProductRepository productRepository, ProductImageRepository productImageRepository) {
        WebDriverManager.chromedriver().setup();
        this.userRepository = userRepository;
        this.fileServerRepository = fileServerRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    // Crawl và lưu vào database
    @Transactional
    public void crawlAndDownloadImages(String keyword) throws InterruptedException {
        // get admin user
        User user = userRepository.findByEmail("admin@gmail.com")
                .orElseThrow(() -> new ResouceNotFoundException("Not found user"));

        // get all products
        // List<Product> products = productRepository.findAll();
        List<Product> products = productRepository.findByProductIdBetween(78L, 177L);

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
            byte[] data = downloadImageAsBytes(url);
            FileServer fileServer = new FileServer();
            fileServer.setType("image/png");
            fileServer.setData(data);
            fileServer.setUser(user);
            fileServerRepository.save(fileServer);

            insertImageForProduct("/api/v1/files/" + fileServer.getId());
        }

        driver.quit(); // Đóng trình duyệt khi hoàn thành
    }

    // Thêm ảnh cho product
    private void insertImageForProduct(String imageUrl) {
        // get all products
        // List<Product> products = productRepository.findAll();
        List<Product> products = productRepository.findByProductIdBetween(78L, 177L);

        for (Product product : products) {
            if (product.getImages().size() >= 5) {
                continue;
            }
            ProductImage productImage = new ProductImage();
            if (product.getImages().isEmpty()) {
                productImage.setImageType(ProductImage.ImageType.MAIN);
            } else {
                productImage.setImageType(ProductImage.ImageType.SUB);
            }
            productImage.setImageUrl(imageUrl);
            productImage.setProduct(product);
            product.getImages().add(productImage);
            productRepository.save(product);
            break;
        }
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

    // Tải ảnh từ URL và chuyển thành mảng byte
    private byte[] downloadImageAsBytes(String imageUrl) {
        // Tải ảnh từ URL và chuyển thành mảng byte
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = httpConn.getInputStream();
                return IOUtils.toByteArray(is);
            }
        } catch (IOException e) {
            log.error("Error downloading image: " + e.getMessage());
        }
        return null;
    }
}
