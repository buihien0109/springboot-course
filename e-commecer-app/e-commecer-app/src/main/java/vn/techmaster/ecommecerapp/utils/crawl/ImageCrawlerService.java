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
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static vn.techmaster.ecommecerapp.service.FileServerService.uploadDir;

@Slf4j
@Service
public class ImageCrawlerService {
    private final int MAX_SIZE_MB = 1;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    // Khởi tạo trình duyệt Chrome
    public ImageCrawlerService(UserRepository userRepository, ProductRepository productRepository) {
        WebDriverManager.chromedriver().setup();
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // Crawl và lưu vào database
    @Transactional
    public void crawlAndDownloadImages(String keyword) throws InterruptedException {
        // get admin user
        User user = userRepository.findByEmail("admin@gmail.com")
                .orElseThrow(() -> new ResouceNotFoundException("Not found user"));

        WebDriver driver = new ChromeDriver();
        driver.get("https://unsplash.com/s/photos/" + keyword);


        // Cuộn trang để kích hoạt lazy loading
        int downloadedImageCount = 0;
        int maxImageCount = 200;
        List<String> urls = new ArrayList<>();

        while (downloadedImageCount < maxImageCount) {
            // Lấy các phần tử hình ảnh
            List<WebElement> imageElements = driver.findElements(By.cssSelector("img.tB6UZ.a5VGX"));
            log.info("Found " + imageElements.size() + " images");

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
            String fileName = String.valueOf(System.currentTimeMillis());
            Path userPath = Path.of(uploadDir, user.getUserId().toString());
            if (!userPath.toFile().exists()) {
                userPath.toFile().mkdirs();
            }
            Path filePath = Path.of(userPath.toString(), fileName);

            // Lưu file vào folder
            try {
                IOUtils.write(data, Files.newOutputStream(filePath));
            } catch (IOException e) {
                log.error("Không thể lưu file");
                log.error(e.getMessage());
            }
        }

        driver.quit(); // Đóng trình duyệt khi hoàn thành
    }

    // Thêm ảnh cho product
    public void insertImageForProduct() {
        // get all products
        List<Product> products = productRepository.findAll();

        // Lấy đường dẫn file tương ứng với user_id
        long userId = 1;
        Path userPath = Path.of(uploadDir, String.valueOf(userId));

        // Lấy ds file trong folder
        List<File> files = List.of(Objects.requireNonNull(userPath.toFile().listFiles()));

        // Tra về ds đường dẫn với từng file
        List<String> filePaths =  files
                .stream()
                .map(File::getName)
                .sorted(Comparator.reverseOrder())
                .map(file -> "/" + uploadDir + "/" + userId + "/" + file)
                .toList();

        Random random = new Random();
        for (Product product : products) {
            String imageImage = filePaths.get(random.nextInt(filePaths.size()));
            List<String> subImages = new ArrayList<>();
            // Lấy 3 -> 5 ảnh ngẫu nhiên từ danh sách ảnh
            for (int i = 0; i < random.nextInt(3) + 3; i++) {
                if(subImages.isEmpty()) {
                    subImages.add(imageImage);
                } else {
                    String subImage = filePaths.get(random.nextInt(filePaths.size()));
                    if(!subImages.contains(subImage)) {
                        subImages.add(subImage);
                    }
                }
            }
            product.setMainImage(imageImage);
            product.setSubImages(subImages);
            productRepository.save(product);
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
