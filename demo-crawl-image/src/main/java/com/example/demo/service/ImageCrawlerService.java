package com.example.demo.service;

import com.example.demo.entity.Image;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class ImageCrawlerService {
    private final int MAX_SIZE_MB = 1;
    private final String DOWNLOAD_PATH = System.getProperty("user.dir") + "/downloads";
    private final ImageService imageService;

    // Tạo thư mục download nếu chưa tồn tại
    private void createUploadFolder(String uploadPath) {
        File uploadFolder = new File(uploadPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
    }

    // Khởi tạo trình duyệt Chrome
    public ImageCrawlerService(ImageService imageService) {
        createUploadFolder(DOWNLOAD_PATH);
        WebDriverManager.chromedriver().setup();
        this.imageService = imageService;
    }

    // Crawl và lưu vào database
    public void crawlAndDownloadImages(String keyword, String downloadPath, int maxImageCount) {
        WebDriver driver = new ChromeDriver();
        driver.get("https://unsplash.com/s/photos/" + keyword);

        int downloadedImageCount = 0;

        while (downloadedImageCount < maxImageCount) {
            // Cuộn trang để kích hoạt lazy loading
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

            // Lấy các phần tử hình ảnh
            List<WebElement> imageElements = driver.findElements(By.cssSelector("img.tB6UZ.a5VGX"));

            for (WebElement imageElement : imageElements) {
                String imageUrl = imageElement.getAttribute("src");
                if (imageUrl != null && !imageUrl.isEmpty() && isImageSizeValid(imageUrl, MAX_SIZE_MB)) {
                    byte[] data = downloadImageAsBytes(imageUrl);
                    Image image = imageService.saveImage(data);
                    downloadedImageCount++;
                }

                if (downloadedImageCount >= maxImageCount) {
                    break;
                }
            }
        }

        driver.quit(); // Đóng trình duyệt khi hoàn thành
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

    // Tải ảnh từ URL và lưu vào thư mục download
    private void downloadImage(String imageUrl, String downloadPath, int maxSizeMB) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                int contentLength = httpConn.getContentLength();
                int maxSizeBytes = maxSizeMB * 1024 * 1024;
                if (contentLength <= maxSizeBytes) {
                    String fileName = randomString(10) + ".jpg";
                    File imageFile = new File(downloadPath, fileName);
                    FileUtils.copyURLToFile(url, imageFile);
                    System.out.println("Downloaded image: " + fileName);
                }
            }
        } catch (IOException e) {
            log.error("Error downloading image: " + e.getMessage());
        }
    }

    // Tạo chuỗi ngẫu nhiên
    private String randomString(int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            result.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return result.toString();
    }
}
