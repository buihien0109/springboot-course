package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import vn.techmaster.ecommecerapp.entity.*;
import vn.techmaster.ecommecerapp.repository.CategoryRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.SupplierRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class ProductTests {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private Slugify slugify;
    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void save_products() {
        // Create 10 products, each product has 4 attributes, 4 images, 1 category using Faker
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            Product product = new Product();
            product.setName(faker.commerce().productName());
            product.setDescription(faker.commerce().material());
            product.setPrice(faker.number().numberBetween(1000, 100000));
            product.setStockQuantity(faker.number().numberBetween(10, 20));

            // Create 4 attributes for each product
            ProductAttribute attribute1 = new ProductAttribute();
            attribute1.setAttributeName("Xuất Xứ");
            attribute1.setAttributeValue(faker.country().name());

            ProductAttribute attribute2 = new ProductAttribute();
            attribute2.setAttributeName("Thành Phần");
            attribute2.setAttributeValue(faker.commerce().material());

            ProductAttribute attribute3 = new ProductAttribute();
            attribute3.setAttributeName("Hướng Dẫn Sử Dụng");
            attribute3.setAttributeValue(faker.commerce().material());

            ProductAttribute attribute4 = new ProductAttribute();
            attribute4.setAttributeName("Bảo Quản");
            attribute4.setAttributeValue(faker.commerce().material());

            // Create 4 images for each product, has 1 main image and 3 sub images
             ProductImage image1 = new ProductImage();
             image1.setImageUrl(faker.company().logo());
             image1.setImageType(ProductImage.ImageType.MAIN);

             ProductImage image2 = new ProductImage();
             image2.setImageUrl(faker.company().logo());
             image2.setImageType(ProductImage.ImageType.SUB);

             ProductImage image3 = new ProductImage();
             image3.setImageUrl(faker.company().logo());
                image3.setImageType(ProductImage.ImageType.SUB);

             ProductImage image4 = new ProductImage();
             image4.setImageUrl(faker.company().logo());
             image4.setImageType(ProductImage.ImageType.SUB);

            // random 1 sub category for each product
            Category category = categoryRepository.findById((long) faker.number().numberBetween(10, 12)).get();

            // Set relationship
            attribute1.setProduct(product);
            attribute2.setProduct(product);
            attribute3.setProduct(product);
            attribute4.setProduct(product);

            image1.setProduct(product);
            image2.setProduct(product);
            image3.setProduct(product);
            image4.setProduct(product);

            product.setCategory(category);

            // Save to database
            product.getAttributes().add(attribute1);
            product.getAttributes().add(attribute2);
            product.getAttributes().add(attribute3);
            product.getAttributes().add(attribute4);

            product.getImages().add(image1);
            product.getImages().add(image2);
            product.getImages().add(image3);
            product.getImages().add(image4);

            product.setCategory(category);
            productRepository.save(product);
        }
    }

    @Transactional
    @Test
    void insert_an_product_attribute_to_product() {
        // Insert an attribute to an existing product
        Product product = productRepository.findById(1L).get();
        ProductAttribute attribute = new ProductAttribute();
        attribute.setAttributeName("Xuất Xứ");
        attribute.setAttributeValue("Việt Nam");
        attribute.setProduct(product);
        product.getAttributes().add(attribute);

        System.out.println(attribute);
        productRepository.save(product);
    }

    @Transactional
    @Test
    void insert_an_product_image_to_product() {
        // Insert an image to an existing product
        Product product = productRepository.findById(1L).get();
        ProductImage image = new ProductImage();
        image.setImageUrl("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.istockphoto.com%2Fvn%2Fphotos%2F%25C4%2591%25E1%25BB%2591i-h%25E1%25BB%258Dc&psig=AOvVaw0QZ3Z3Z2Z2Z2Z2Z2Z2Z2Z2&ust=1629788450008000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCJjQ1ZqBzvICFQAAAAAdAAAAABAD");
        image.setImageType(ProductImage.ImageType.MAIN);
        image.setProduct(product);
        product.getImages().add(image);

        System.out.println(image);
        productRepository.save(product);
    }

    @Test
    void update_slug() {
        // update all slug of products
        for (Product product : productRepository.findAll()) {
            product.setSlug(slugify.slugify(product.getName()));
            productRepository.save(product);
        }
    }

    @Test
    void update_supplier() {
        Random random = new Random();
        List<Supplier> suppliers = supplierRepository.findAll();

        // for each product, random a supplier
        for (Product product : productRepository.findAll()) {
            product.setSupplier(suppliers.get(random.nextInt(suppliers.size())));
            productRepository.save(product);
        }
    }

    @Test
    void update_stock_quantity() {
        Faker faker = new Faker();
        // update stock quantity of all products
        for (Product product : productRepository.findAll()) {
            if (product.getStatus() == Product.Status.AVAILABLE) {
                product.setStockQuantity(faker.number().numberBetween(10, 30));
            } else {
                product.setStockQuantity(0);
            }
            productRepository.save(product);
        }
    }

    @Test
    void update_price_product() {
        // update price of all products
        for (Product product : productRepository.findAll()) {
            product.setPrice(randomPrice());
            productRepository.save(product);
        }
    }

    // viết method để random số từ 10000 đến 1000000 và làm tròn đến hàng nghìn
    public int randomPrice() {
        Random random = new Random();
        int price = random.nextInt(100000 - 10000 + 1) + 10000;
        price = price / 1000;
        price = price * 1000;
        return price;
    }
}
