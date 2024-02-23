package vn.techmaster.ecommecerapp.utils.crawl;

import com.github.slugify.Slugify;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.*;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.repository.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static vn.techmaster.ecommecerapp.service.FileServerService.uploadDir;

@Service
@Slf4j
public class ImageCrawlerServiceWinMart {
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final int MAX_SIZE_MB = 1;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final ReviewRepository reviewRepository;
    private final Slugify slugify;

    // Khởi tạo trình duyệt Chrome
    public ImageCrawlerServiceWinMart(UserRepository userRepository, ProductRepository productRepository, ProductAttributeRepository productAttributeRepository, Slugify slugify, ReviewRepository reviewRepository,
                                      CategoryRepository categoryRepository,
                                      SupplierRepository supplierRepository) {
        WebDriverManager.chromedriver().setup();
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productAttributeRepository = productAttributeRepository;
        this.slugify = slugify;
        this.reviewRepository = reviewRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public void openWeb() {
        Random random = new Random();
        List<Map<String, Object>> categoryList = new ArrayList<>(
                List.of(
//                        Map.of("name", "Rau lá", "url", "https://www.bachhoaxanh.com/rau-sach"),
//                        Map.of("name", "Củ quả", "url", "https://www.bachhoaxanh.com/cu"),
//                        Map.of("name", "Trái cây tươi", "url", "https://www.bachhoaxanh.com/trai-cay-tuoi-ngon"),

//                        Map.of("name", "Sữa tươi", "url", "https://www.bachhoaxanh.com/sua-tuoi"),
//                        Map.of("name", "Sữa bột", "url", "https://www.bachhoaxanh.com/sua-bot-cong-thuc"),
//                        Map.of("name", "Sữa đặc", "url", "https://www.bachhoaxanh.com/sua-dac"),
//                        Map.of("name", "Sữa hạt - Sữa đậu", "url", "https://www.bachhoaxanh.com/sua-tu-hat"),
//                        Map.of("name", "Bơ sữa - Phô mai", "url", "https://www.bachhoaxanh.com/pho-mai-an"),
//                        Map.of("name", "Sữa chua - Váng sữa", "url", "https://www.bachhoaxanh.com/sua-chua-an"),

//                        Map.of("name", "Bánh xốp - Bánh quy", "url", "https://www.bachhoaxanh.com/banh-xop"),
//                        Map.of("name", "Kẹo - Chocolate", "url", "https://www.bachhoaxanh.com/socola"),
//                        Map.of("name", "Bánh Snack", "url", "https://www.bachhoaxanh.com/snack"),
//                        Map.of("name", "Hạt - Trái cây sấy khô", "url", "https://www.bachhoaxanh.com/trai-cay-say"),
//
//                        Map.of("name", "Cafe", "url", "https://www.bachhoaxanh.com/ca-phe-hoa-tan"),
//                        Map.of("name", "Nước suối", "url", "https://www.bachhoaxanh.com/nuoc-suoi-khoang"),
//                        Map.of("name", "Nước ngọt", "url", "https://www.bachhoaxanh.com/nuoc-ngot"),
//                        Map.of("name", "Trà - Các loại khác", "url", "https://www.bachhoaxanh.com/tra-kho-tui-loc"),
//                        Map.of("name", "Bia", "url", "https://www.bachhoaxanh.com/bia"),
//
//                        Map.of("name", "Mỳ", "url", "https://www.bachhoaxanh.com/mi"),
//                        Map.of("name", "Miến - Hủ tiếu - Bánh canh", "url", "https://www.bachhoaxanh.com/hu-tieu"),
//                        Map.of("name", "Cháo", "url", "https://www.bachhoaxanh.com/chao-an-lien"),
//                        Map.of("name", "Phở - Bún", "url", "https://www.bachhoaxanh.com/pho"),
//
//                        Map.of("name", "Gạo - Nông sản khô", "url", "https://www.bachhoaxanh.com/gao-gao-nep"),
//                        Map.of("name", "Ngũ cốc - Yến mạch", "url", "https://www.bachhoaxanh.com/bot-ngu-coc"),
//                        Map.of("name", "Thực phẩm đóng hộp", "url", "https://www.bachhoaxanh.com/thit-heo-hop"),
//                        Map.of("name", "Rong biển - Tảo biển", "url", "https://www.bachhoaxanh.com/rong-bien"),
//                        Map.of("name", "Bột các loại", "url", "https://www.bachhoaxanh.com/bot-che-bien-san"),
//                        Map.of("name", "Thực phẩm chay", "url", "https://www.bachhoaxanh.com/mi-chay"),
//
//                        Map.of("name", "Bánh bao", "url", "https://www.bachhoaxanh.com/banh-dong-cac-loai"),
//                        Map.of("name", "Xúc xích - Thịt nguội", "url", "https://www.bachhoaxanh.com/xuc-xich-tuoi"),
//                        Map.of("name", "Bánh mỳ", "url", "https://www.bachhoaxanh.com/ha-cao-sui-cao-xiu-mai"),
//                        Map.of("name", "Kim chi", "url", "https://www.bachhoaxanh.com/do-chua-dua-muoi"),
//
//                        Map.of("name", "Hải sản đông lạnh", "url", "https://www.bachhoaxanh.com/ca-tom-muc-ech"),
//                        Map.of("name", "Thịt đông lạnh", "url", "https://www.bachhoaxanh.com/thit-heo"),
//                        Map.of("name", "Chả giò", "url", "https://www.bachhoaxanh.com/cha-gio"),
//                        Map.of("name", "Cá - Bò viên", "url", "https://www.bachhoaxanh.com/ca-vien-bo-vien"),

//                        Map.of("name", "Dầu ăn", "url", "https://www.bachhoaxanh.com/dau-an"),
//                        Map.of("name", "Nước mắm - Nước chấm", "url", "https://www.bachhoaxanh.com/nuoc-mam"),
//                        Map.of("name", "Đường", "url", "https://www.bachhoaxanh.com/duong"),
//                        Map.of("name", "Nước tương", "url", "https://www.bachhoaxanh.com/nuoc-tuong"),
//                        Map.of("name", "Hạt nêm", "url", "https://www.bachhoaxanh.com/gia-vi-nem-san"),
//
//                        Map.of("name", "Trứng", "url", "https://www.bachhoaxanh.com/trung"),
//                        Map.of("name", "Đậu hũ", "url", "https://www.bachhoaxanh.com/dau-hu-dau-hu-trung")
                )
        );

        List<Map<String, String>> urlsProductOfCategory = new ArrayList<>();

        try {
            for (Map<String, Object> category : categoryList) {
                WebDriver driver = new ChromeDriver();
                driver.get(category.get("url").toString());
                Thread.sleep(5000);
                List<WebElement> elements = driver.findElements(By.cssSelector("a.relative.w-full.text-center"));

                // Random 1 number from 10 -> 20
                int start = 0;
                int end = Math.min(10 + random.nextInt(10), elements.size());
                while (start < end) {
                    WebElement element = elements.get(start);
                    String url = element.getAttribute("href");
                    Map<String, String> productUrl = new HashMap<>();
                    productUrl.put("category", category.get("name").toString());
                    productUrl.put("url", url);
                    urlsProductOfCategory.add(productUrl);
                    start++;
                }
                driver.quit();
            }

            for (Map<String, String> productUrl : urlsProductOfCategory) {
                log.info("\nCategory: " + productUrl.get("category"));
                log.info("URL: " + productUrl.get("url"));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Crawl và lưu vào database
        for (Map<String, String> productUrl : urlsProductOfCategory) {
            crawlNewProduct(productUrl);
        }
    }

    // Crawl và lưu vào database
    public void crawlNewProduct(Map<String, String> productUrl) {
        Random random = new Random();
        // Get user by email
        User user = userRepository.findByEmail("admin@gmail.com")
                .orElseThrow(() -> new ResouceNotFoundException("Not found user"));

        // Get users has role USER
        List<User> users = userRepository.findByRoles_NameIn(List.of("USER"));

        // Get all suppliers
        List<Supplier> suppliers = supplierRepository.findAll();
        Supplier rdSupplier = suppliers.get(random.nextInt(suppliers.size()));

        WebDriver driver = new ChromeDriver();
        driver.get(productUrl.get("url"));

        try {
            Thread.sleep(5000);

            // Crawling Product Information
            String productName = driver.findElement(By.cssSelector("h1.text-20.font-bold")).getText();
            String productSlug = slugify.slugify(productName);
            String productDescriptionStr = driver.findElement(By.cssSelector("div.text-14.leading-5")).getText();
            String productDescription = Jsoup.parse(productDescriptionStr).text();

            // Giá sản phẩm
            int productPrice = 0;
            WebElement elementPrice = null;
            List<WebElement> elementsPrice = driver.findElements(By.cssSelector("div.text-20.font-bold.text-red-price"));
            log.info("elementsPrice: " + elementsPrice.size());
            if (!elementsPrice.isEmpty()) {
                log.info("has elements price");
                elementPrice = elementsPrice.get(0);
                productPrice = Integer.parseInt(elementPrice.getText().replace("₫", "").replace(".", "").trim());
            }

            // Thuộc tính sản phẩm
            Map<String, String> productAttributes = new HashMap<>();
            WebElement elementTable = null;
            List<WebElement> elementsTable = driver.findElements(By.cssSelector("table.mt-3.text-14.leading-5"));
            System.out.println("elementsTable: " + elementsTable.size());
            if (!elementsTable.isEmpty()) {
                log.info("has elements table");
                elementTable = elementsTable.get(0);
                List<WebElement> elementsTr = elementTable.findElements(By.cssSelector("tr"));
                log.info("elementsTr: " + elementsTr.size());
                for (WebElement elementTr : elementsTr) {
                    List<WebElement> elementsTd = elementTr.findElements(By.cssSelector("td"));
                    log.info("elementsTd: " + elementsTd.size());
                    productAttributes.put(elementsTd.get(0).getText(), elementsTd.get(1).getText());
                }
            }

            // Crawling review
            WebElement iframe = driver.findElement(By.id("id-rating"));
            driver.switchTo().frame(iframe);

            List<WebElement> elementsReview = driver.findElements(By.cssSelector("ul.comment-list"));
            log.info("elementsReview: " + elementsReview.size());

            List<Review> reviews = new ArrayList<>();
            if (!elementsReview.isEmpty()) {
                log.info("has review elements");
                WebElement elementReview = elementsReview.get(0);
                List<WebElement> reviewItems = elementReview.findElements(By.cssSelector("li.par"));
                log.info("reviewItems: " + reviewItems.size());
                for (WebElement reviewItem : reviewItems) {
                    String reviewContent = reviewItem.findElement(By.cssSelector(".cmt-content .cmt-txt")).getText();
                    List<WebElement> reviewStars = reviewItem.findElements(By.cssSelector(".cmt-top-star .iconcmt-starbuy"));
                    int starCount = reviewStars.size();

                    Review review = new Review();
                    review.setComment(reviewContent);
                    review.setRating(starCount);
                    review.setStatus(Review.Status.ACCEPTED);

                    // Random user
                    int randomIndex = random.nextInt(users.size());
                    review.setUser(users.get(randomIndex));

                    reviews.add(review);
                }
            }

            // Quay lại context mặc định
            driver.switchTo().defaultContent();

            // Crawling images
            List<WebElement> imageElements = driver.findElements(By.cssSelector("div.relative.inline-block.w-full.cursor-zoom-in.object-contain img"));
            int downloadedImageCount = 0;
            int maxImageCount = imageElements.size();
            List<String> urls = new ArrayList<>();

            while (downloadedImageCount < maxImageCount) {
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
            }

            int count = 0;
            String mainImage = null;
            List<String> subImages = new ArrayList<>();
            for (String url : urls) {
                byte[] data = downloadImageAsBytes(url);
                String fileName = String.valueOf(System.currentTimeMillis());
                Path userPath = Path.of(uploadDir, user.getUserId().toString());
                if (!userPath.toFile().exists()) {
                    userPath.toFile().mkdirs();
                }
                Path filePath = Path.of(userPath.toString(), fileName);

                // Lưu file vào folder

                IOUtils.write(data, Files.newOutputStream(filePath));

                if (count == 0) {
                    log.info("Main image: " + filePath);
                    mainImage = "/" + uploadDir + "/" + user.getUserId() + "/" + fileName;
                    count++;
                } else {
                    log.info("Sub image: " + filePath);
                    subImages.add("/" + uploadDir + "/" + user.getUserId() + "/" + fileName);
                }
            }

            // Save product
            Product product = new Product();
            product.setName(productName);
            product.setSlug(productSlug);
            product.setDescription(productDescription);
            if (productPrice != 0) {
                product.setPrice(productPrice);
            } else {
                product.setPrice(randomPrice());
            }
            product.setMainImage(mainImage);
            product.setSubImages(subImages);
            product.setStatus(Product.Status.AVAILABLE);
            product.setStockQuantity(random.nextInt(50 - 20 + 1) + 20);

            Category category = categoryRepository.findByName(productUrl.get("category"))
                    .orElseThrow(() -> new ResouceNotFoundException("Not found category"));
            product.setCategory(category);

            product.setSupplier(rdSupplier);

            productRepository.save(product);

            // Save product attributes
            if (!productAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : productAttributes.entrySet()) {
                    ProductAttribute productAttribute = new ProductAttribute();
                    productAttribute.setAttributeName(entry.getKey());
                    productAttribute.setAttributeValue(entry.getValue());
                    productAttribute.setProduct(product);
                    productAttributeRepository.save(productAttribute);
                }
            }

            // Save reviews
            for (Review review : reviews) {
                review.setProduct(product);
                reviewRepository.save(review);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public int randomPrice() {
        Random random = new Random();
        int price = random.nextInt(100000 - 10000 + 1) + 10000;
        price = price / 1000;
        price = price * 1000;
        return price;
    }

    // Crawl và lưu vào database
    @Transactional
    public void crawlAndDownloadImages(String webUrl, Long productId) {
        Random random = new Random();
        // Get user by email
        User user = userRepository.findByEmail("admin@gmail.com")
                .orElseThrow(() -> new ResouceNotFoundException("Not found user"));

        // Get users has role USER
        List<User> users = userRepository.findByRoles_NameIn(List.of("USER"));

        // Get product by id
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResouceNotFoundException("Not found product"));

        WebDriver driver = new ChromeDriver();
        driver.get(webUrl);

        try {
            Thread.sleep(10000);

            // Crawling Product Information
            String productName = driver.findElement(By.cssSelector("h1.text-20.font-bold")).getText();
            String productSlug = slugify.slugify(productName);
            String productDescriptionStr = driver.findElement(By.cssSelector("div.text-14.leading-5")).getText();
            String productDescription = Jsoup.parse(productDescriptionStr).text();

            // Giá sản phẩm
            int productPrice = 0;
            WebElement elementPrice = null;
            List<WebElement> elementsPrice = driver.findElements(By.cssSelector("div.text-20.font-bold.text-red-price"));
            log.info("elementsPrice: " + elementsPrice.size());
            if (!elementsPrice.isEmpty()) {
                log.info("has elements price");
                elementPrice = elementsPrice.get(0);
                productPrice = Integer.parseInt(elementPrice.getText().replace("₫", "").replace(".", "").trim());
            }

            // Thuộc tính sản phẩm
            Map<String, String> productAttributes = new HashMap<>();
            WebElement elementTable = null;
            List<WebElement> elementsTable = driver.findElements(By.cssSelector("table.mt-3.text-14.leading-5"));
            System.out.println("elementsTable: " + elementsTable.size());
            if (!elementsTable.isEmpty()) {
                log.info("has elements table");
                elementTable = elementsTable.get(0);
                List<WebElement> elementsTr = elementTable.findElements(By.cssSelector("tr"));
                log.info("elementsTr: " + elementsTr.size());
                for (WebElement elementTr : elementsTr) {
                    List<WebElement> elementsTd = elementTr.findElements(By.cssSelector("td"));
                    log.info("elementsTd: " + elementsTd.size());
                    productAttributes.put(elementsTd.get(0).getText(), elementsTd.get(1).getText());
                }
            }

            // Crawling review
            WebElement iframe = driver.findElement(By.id("id-rating"));
            driver.switchTo().frame(iframe);

            List<WebElement> elementsReview = driver.findElements(By.cssSelector("ul.comment-list"));
            log.info("elementsReview: " + elementsReview.size());

            if (!elementsReview.isEmpty()) {
                log.info("has review elements");
                WebElement elementReview = elementsReview.get(0);
                List<WebElement> reviewItems = elementReview.findElements(By.cssSelector("li.par"));
                log.info("reviewItems: " + reviewItems.size());
                for (WebElement reviewItem : reviewItems) {
                    String reviewContent = reviewItem.findElement(By.cssSelector(".cmt-content .cmt-txt")).getText();
                    List<WebElement> reviewStars = reviewItem.findElements(By.cssSelector(".cmt-top-star .iconcmt-starbuy"));
                    int starCount = reviewStars.size();

                    Review review = new Review();
                    review.setComment(reviewContent);
                    review.setRating(starCount);
                    review.setProduct(product);
                    review.setStatus(Review.Status.ACCEPTED);

                    // Random user
                    int randomIndex = random.nextInt(users.size());
                    review.setUser(users.get(randomIndex));

                    reviewRepository.save(review);
                }
            }

            // Quay lại context mặc định
            driver.switchTo().defaultContent();

            // Crawling images
            List<WebElement> imageElements = driver.findElements(By.cssSelector("div.relative.inline-block.w-full.cursor-zoom-in.object-contain img"));
            int downloadedImageCount = 0;
            int maxImageCount = imageElements.size();
            List<String> urls = new ArrayList<>();

            while (downloadedImageCount < maxImageCount) {
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
            }

            int count = 0;
            String mainImage = null;
            List<String> subImages = new ArrayList<>();
            for (String url : urls) {
                byte[] data = downloadImageAsBytes(url);
                String fileName = String.valueOf(System.currentTimeMillis());
                Path userPath = Path.of(uploadDir, user.getUserId().toString());
                if (!userPath.toFile().exists()) {
                    userPath.toFile().mkdirs();
                }
                Path filePath = Path.of(userPath.toString(), fileName);

                // Lưu file vào folder

                IOUtils.write(data, Files.newOutputStream(filePath));

                if (count == 0) {
                    log.info("Main image: " + filePath);
                    mainImage = "/" + uploadDir + "/" + user.getUserId() + "/" + fileName;
                    count++;
                } else {
                    log.info("Sub image: " + filePath);
                    subImages.add("/" + uploadDir + "/" + user.getUserId() + "/" + fileName);
                }
            }
            product.setName(productName);
            product.setSlug(productSlug);
            product.setDescription(productDescription);
            if (productPrice != 0) {
                product.setPrice(productPrice);
            }
            product.setMainImage(mainImage);
            product.setSubImages(subImages);

            log.info("productAttributes size: " + productAttributes.size());
            if (!productAttributes.isEmpty()) {
                // Remove all attributes
                product.getAttributes().forEach(productAttribute -> {
                    productAttribute.setProduct(null);
                });
                product.getAttributes().clear();

                for (Map.Entry<String, String> entry : productAttributes.entrySet()) {
                    ProductAttribute productAttribute = new ProductAttribute();
                    productAttribute.setAttributeName(entry.getKey());
                    productAttribute.setAttributeValue(entry.getValue());
                    productAttribute.setProduct(product);
                    productAttributeRepository.save(productAttribute);
                }
            }
            productRepository.save(product);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
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
